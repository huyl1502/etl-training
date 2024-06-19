package com.flinters.etl.repository.MySQLRepository

import com.typesafe.config.ConfigFactory
import slick.jdbc.JdbcBackend.Database

object DBConnection {

  // Load the configuration
  private val config = ConfigFactory.load("application.conf")
  val db             = Database.forConfig(path = "mydb.db", config = config)
}
