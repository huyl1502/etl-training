package com.flinters.etl.service

import com.flinters.etl.model.{Account, FileDataExtract, FileDataTransform, FileInfo}
import com.flinters.etl.repository.FileSystemRepository.FSRepository
import com.flinters.etl.repository.MySQLRepository.PlatformDao

import java.io.File
import java.nio.file.Path
import scala.util.matching.Regex

class FirstETLHandler(fsRepository: FSRepository) extends BaseETL(fsRepository) {

  val FNAME_REGEX: Regex = """(\d+)[：:]([a-zA-Z]{3})-(\d+)[：:]([^：:]+)?[：:]([^：:]+)\.csv""".r

  override def extract(file: File): FileDataExtract = {
    val (headers, rows)                 = fsRepository.readFile(file.getPath)
    if (rows.isEmpty) throw new Exception(s"File ${file.getName} have no row")
    val (reportDate, account, platform) = file.getName match {
      case FNAME_REGEX(reportDate, platformCode, accountId, adAccountId, accountName) =>
        val platformDao = new PlatformDao
        val platform    = platformDao.getPlatformById(platformCode)
        if (platform == null) throw new Exception(s"File ${file.getName} have invalid platformCode")
        (reportDate, Account(accountId, accountName, adAccountId), platform)
    }

    FileDataExtract(file.getName, reportDate, account, platform, headers, rows)
  }

  override def load(
    fileInfo:         FileInfo,
    filteredFilePath: String,
    filteredData:     FileDataTransform,
    outputPath:       Path,
    outputData:       FileDataTransform
  ): Unit = {
    fsRepository.writeFile(filteredFilePath, filteredData.fileHeaders, filteredData.fileRows)

    val outputFilePath = outputPath.resolve(s"(1) ${fileInfo.platform.id} - ${fileInfo.account.accountId}.csv").toString
    fsRepository.writeFile(outputFilePath, outputData.fileHeaders, outputData.fileRows)
  }

  override protected def canHandle(file: File): Boolean = {
    file.getName match {
      case FNAME_REGEX(_, _, _, _, _) => true
      case _                          => false
    }
  }
}
