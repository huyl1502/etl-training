package com.flinters.etl.repository.FileSystemRepository

import com.flinters.etl.model.FileDataRow
import org.apache.commons.csv.CSVRecord

trait FSRepository {

  def readFile(filePath: String): (List[String], List[FileDataRow])

  def writeFile(filePath: String, rows: List[List[String]]): Unit
}
