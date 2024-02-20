ThisBuild / scalaVersion     := "2.13.11"
ThisBuild / version          := "1.0.0"
ThisBuild / organization     := "com.vn.flinters"
ThisBuild / organizationName := "flinters-vn"

lazy val root = (project in file("."))
  .settings(
    name              := "etl-training",
    scalafmtOnCompile := true,
    libraryDependencies ++= Seq(
      "com.typesafe"          % "config"               % "1.4.2",
      "mysql"                 % "mysql-connector-java" % "8.0.33"
    )
  )
