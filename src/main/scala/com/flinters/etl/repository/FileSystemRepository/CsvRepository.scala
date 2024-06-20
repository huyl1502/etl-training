package com.flinters.etl.repository.FileSystemRepository

import com.flinters.etl.model.FileDataRow
import org.apache.commons.csv.{CSVFormat, CSVParser, CSVPrinter}

import java.io.FileWriter
import scala.io.Source
import scala.jdk.CollectionConverters.CollectionHasAsScala

class CsvRepository extends FSRepository {

  override def readFile(filePath: String): (List[String], List[FileDataRow]) = {
    try {
      val source    = Source.fromFile(filePath)
      val csvParser = CSVParser.parse(source.mkString, CSVFormat.DEFAULT.withHeader())

      // Extract the records
      val headers = csvParser.getHeaderNames.asScala.toList
      val records = csvParser.getRecords.asScala.toList.map(record =>
        FileDataRow(data = headers.map(header => header -> record.get(header)).toMap))
      (headers, records)
    } catch {
      case ex: Exception =>
        throw new Exception(s"Failed to read file: ${filePath}")
    }
  }

  override def writeFile(filePath: String, headers: List[String], rows: List[List[String]]): Unit = {
    // Create a FileWriter
    val fileWriter = new FileWriter(filePath)

    // Define the CSV format and printer
    val csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader(headers: _*))

    // Write records to the CSV file
    rows.foreach(row => csvPrinter.printRecord(row: _*))

    // Flush and close the printer
    csvPrinter.flush()
    csvPrinter.close()
  }
}
