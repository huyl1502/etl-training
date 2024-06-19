package com.flinters.etl

import com.flinters.etl.model.ActionLog
import com.flinters.etl.repository.FileSystemRepository.CsvRepository
import com.flinters.etl.repository.MySQLRepository.ActionLogDao
import com.flinters.etl.service.{FirstETLHandler, SecondETLHandler}

import java.nio.file.{Files, Paths}
import scala.Console.{RED, RESET}

object Main {

  private val fsRepository = new CsvRepository()

  private val firstETLHandler  = new FirstETLHandler(fsRepository)
  private val secondETLHandler = new SecondETLHandler(fsRepository)

  def main(args: Array[String]): Unit = {
    try {
      prepareTestData() // Generate 2001 input files
    } catch {
      case ex: Exception =>
        println(s"An unexpected error occurred: ${ex.getMessage}")
    }

    val workDir    = Paths.get("workspace/input").toFile
    val inputFiles = workDir.listFiles().toSeq

    println(s"========/ Start to handle ${inputFiles.length} inputFiles /========")
    val startTime = System.currentTimeMillis()

    // TODO: Add code here ...
//    val f1     = Future.traverse(inputFiles)(firstETLHandler.handle)
//    val f2     = Future.traverse(inputFiles)(secondETLHandler.handle)
//
//    val result = for { _ <- f1; _ <- f2 } yield ()
//    Await.result(result, Duration.Inf)
//
//    // Handle the result asynchronously
//    result.onComplete {
//      case Success(data) => data
//      case Failure(ex)   => ex
//    }
//
//    println(Await.result(result, Duration.Inf))

    val actionLogDao = new ActionLogDao()
    inputFiles.foreach(file => {
      try {
        val startTimeExeOneFile = System.currentTimeMillis()
        firstETLHandler.handle(file)
        secondETLHandler.handle(file)
        val endTime = System.currentTimeMillis()
        val exeTime = endTime - startTimeExeOneFile
        actionLogDao.insertActionLog(ActionLog(None, file.getName, "Success", exeTime.toString))
      } catch {
        case ex: Exception => {
          println(s"${RED}${ex.getMessage}${RESET}")
          actionLogDao.insertActionLog(ActionLog(None, file.getName, ex.getMessage, ""))
        }
      }
    })

    val endTime = System.currentTimeMillis()
    println(s"Elapsed time: ${endTime - startTime} ms")
    cleanWorkDir() // Clean up the workspace
  }

  // No edit this function
  private def cleanWorkDir(): Unit = {
    val outputDir = Paths.get("workspace/output").toFile
    if (outputDir.exists()) outputDir.listFiles().filterNot(f => f.getName.contains("- 1.")).foreach(_.delete())

    val filteredDir = Paths.get("workspace/filtered").toFile
    if (filteredDir.exists()) filteredDir.listFiles().filterNot(f => f.getName.contains("-1：")).foreach(_.delete())

    (2 to 500).foreach { id =>
      val dest1 = Paths.get(s"workspace/input/20240218：Pin-$id：533：ニトリ_SEPTENI.csv")
      val dest2 = Paths.get(s"workspace/input/Pin-$id：ニトリ_SEPTENI.csv")
      val dest3 = Paths.get(s"workspace/input/20240218：Kan-$id：5335：ニトリ_SEPTENI.csv")
      val dest4 = Paths.get(s"workspace/input/Kan-$id：ニトリ_SEPTENI.csv")

      Files.deleteIfExists(dest1)
      Files.deleteIfExists(dest2)
      Files.deleteIfExists(dest3)
      Files.deleteIfExists(dest4)
    }
  }

  // No edit this function
  private def prepareTestData(): Unit = {
    val src1 = Paths.get("workspace/input/20240218：Pin-1：533：ニトリ_SEPTENI.csv")
    val src2 = Paths.get("workspace/input/Pin-1：ニトリ_SEPTENI.csv")
    val src3 = Paths.get("workspace/input/20240218：Kan-1：5335：ニトリ_SEPTENI.csv")
    val src4 = Paths.get("workspace/input/Kan-1：ニトリ_SEPTENI.csv")

    (2 to 500).foreach { id =>
      val dest1 = Paths.get(s"workspace/input/20240218：Pin-$id：533：ニトリ_SEPTENI.csv")
      val dest2 = Paths.get(s"workspace/input/Pin-$id：ニトリ_SEPTENI.csv")
      val dest3 = Paths.get(s"workspace/input/20240218：Kan-$id：5335：ニトリ_SEPTENI.csv")
      val dest4 = Paths.get(s"workspace/input/Kan-$id：ニトリ_SEPTENI.csv")

      Files.copy(src1, dest1)
      Files.copy(src2, dest2)
      Files.copy(src3, dest3)
      Files.copy(src4, dest4)
    }
  }
}
