package com.flinters.etl.service

import com.flinters.etl.model.{Account, FileDataExtract, FileDataTransform, FileInfo}
import com.flinters.etl.repository.FileSystemRepository.FSRepository
import com.flinters.etl.repository.MySQLRepository.{MediaSettingDao, PlatformDao}

import java.io.File
import java.nio.file.Path

class SecondETLHandler(fsRepository: FSRepository) extends BaseETL(fsRepository) {

  private val FNAME_REGEX = """([a-zA-Z]{3})-(\d+)[：:]([^：:]+)\.csv""".r

  override def extract(file: File): FileDataExtract = {
    val (headers, rows)                 = fsRepository.readFile(file.getPath)
    if (rows.isEmpty) throw new Exception(s"File ${file.getName} have no row")
    val (reportDate, account, platform) = file.getName match {
      case FNAME_REGEX(platformCode, accountId, accountName) =>
        val platformDao     = new PlatformDao
        val mediaSettingDao = new MediaSettingDao
        val platform        = platformDao.getPlatformById(platformCode)
        if (platform == null) throw new Exception(s"File ${file.getName} have invalid platformCode")
        val mediaSetting    = mediaSettingDao.getMediaSettingById(platformCode)

        (rows.head.data(mediaSetting.report_date_col), Account(accountId, accountName, ""), platform)
    }

    FileDataExtract(file.getName, reportDate, account, platform, headers, rows)
  }

  override def load(fileInfo: FileInfo, filteredFilePath: String, filteredData: FileDataTransform, outputPath: Path, outputData: FileDataTransform): Unit = {
    fsRepository.writeFile(filteredFilePath, filteredData.fileRows)

    val outputFilePath = outputPath.resolve(s"(1) ${fileInfo.platform.id} - ${fileInfo.account.accountId} .csv").toString
    fsRepository.writeFile(outputFilePath, outputData.fileRows)
  }

  override protected def canHandle(file: File): Boolean = {
    file.getName match {
      case FNAME_REGEX(_, _, _) => true
      case _                    => false
    }
  }
}
