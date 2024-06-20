package com.flinters.etl.repository.FileSystemRepository

import com.flinters.etl.model.FileDataRow

trait FSRepository {

  def readFile(filePath: String): (List[String], List[FileDataRow])

  def writeFile(filePath: String, headers: List[String], rows: List[List[String]]): Unit
}
