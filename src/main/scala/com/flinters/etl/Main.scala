package com.flinters.etl

import java.nio.file.{ Files, Paths }

object Main {

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

  def main(args: Array[String]): Unit = {
    prepareTestData() // Generate 2001 input files
    val workDir = Paths.get("workspace/input").toFile
    val inputFiles   = workDir.listFiles().toSeq

    println(s"========/ Start to handle ${inputFiles.length} inputFiles /========")
    val startTime = System.currentTimeMillis()

    // TODO: Add code here ...

    val endTime = System.currentTimeMillis()
    println(s"Elapsed time: ${endTime - startTime} ms")
    cleanWorkDir() // Clean up the workspace
  }
}
