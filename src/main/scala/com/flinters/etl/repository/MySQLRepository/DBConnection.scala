package com.flinters.etl.repository.MySQLRepository

import com.typesafe.config.ConfigFactory
import slick.jdbc.JdbcBackend.Database

object DBConnection {

  val db             = Database.forConfig(path = "mydb.db", config = config)
  // Load the configuration
  private val config = ConfigFactory.load("application.conf")
}
