package com.flinters.etl.service

import com.flinters.etl.constant.Constant
import com.flinters.etl.model.{ActionLog, FileDataExtract, FileDataTransform, FileInfo}
import com.flinters.etl.repository.FileSystemRepository.FSRepository
import com.flinters.etl.repository.MySQLRepository.{ActionLogDao, MediaSettingDao}

import java.io.File
import java.nio.file.{Path, Paths}
import scala.Console.{RED, RESET}
import scala.concurrent.Future

abstract class BaseETL(fsRepository: FSRepository) {

  val actionLogDao         = new ActionLogDao()
  private val FILTERED_DIR = Paths.get("workspace").resolve("filtered")
  private val OUTPUT_DIR   = Paths.get("workspace").resolve("output")

  final def handle(file: File): Future[Unit] = {
    try {
      if (canHandle(file)) {
        val startTimeExeOneFile = System.currentTimeMillis()
        val extractedData                        = extract(file)
        val (fileInfo, filteredData, outputData) = transform(extractedData)
        load(fileInfo, FILTERED_DIR.resolve(file.getName).toString, filteredData, OUTPUT_DIR, outputData)
        println(s"Handle file: ${file.getName} successfully")
        val endTime             = System.currentTimeMillis()
        val exeTime             = endTime - startTimeExeOneFile
        actionLogDao.insertActionLog(ActionLog(None, file.getName, "Success", exeTime.toString))
      }
    } catch {
      case ex: Exception =>
        println(s"${RED}${ex.getMessage}${RESET}")
        actionLogDao.insertActionLog(ActionLog(None, file.getName, ex.getMessage, ""))
    }

    Future.successful()
  }

  def transform(data: FileDataExtract): (FileInfo, FileDataTransform, FileDataTransform) = {
    try {
      val mediaSettingDao = new MediaSettingDao
      val mediaSetting    = mediaSettingDao.getMediaSettingById(data.platform.id)
      val filteredColumns = List(mediaSetting.cost_col, mediaSetting.cv_col, mediaSetting.imp_col, mediaSetting.click_col)

      val filteredHeaders  = data.fileHeaders
      val filteredRows     = data.fileRows
        .filter(row => filteredColumns.forall(col => !(row.data(col).toDouble == 0)))
        .map(row => row.data.values.toList)
      val filteredFileData = FileDataTransform(filteredHeaders, filteredRows)

      val outputHeaders    = Constant.OUTPUT_FILE_HEADERS
      val lstOutputFileRow = data.fileRows.map { row =>
        List(
          data.reportDate,
          data.platform.id,
          data.platform.name,
          data.account.accountId,
          data.account.adAccountId,
          data.account.accountName,
          row.data(mediaSetting.imp_col),
          row.data(mediaSetting.click_col),
          (row.data(mediaSetting.cost_col).toDouble * 2).toString,
          row.data(mediaSetting.cv_col)
        )
      }
      val outputFileData   = FileDataTransform(outputHeaders, lstOutputFileRow)

      (FileInfo(data.account, data.platform), filteredFileData, outputFileData)
    } catch {
      case ex: Exception =>
        throw new Exception(s"Failed to transform data from file: ${data.fileName}")
    }
  }

  def extract(file: File): FileDataExtract

  def load(
    fileInfo: FileInfo,
    filteredFilePath: String,
    filteredData: FileDataTransform,
    outputPath: Path,
    outputData: FileDataTransform
  ): Unit

  protected def canHandle(file: File): Boolean
}
