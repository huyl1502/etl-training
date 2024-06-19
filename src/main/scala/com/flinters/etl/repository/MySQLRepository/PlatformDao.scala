package com.flinters.etl.repository.MySQLRepository

import com.flinters.etl.model.Platform
import com.flinters.etl.repository.MySQLRepository.DBConnection.db
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{TableQuery, Tag}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class Platforms(tag: Tag) extends Table[Platform](tag, "platforms") {

  def * =
    (
      id,
      name,
      currency_rate
    ) <> ((Platform.apply _).tupled, Platform.unapply)

  def id            = column[String]("id", O.PrimaryKey, O.Unique)

  def name          = column[String]("name")

  def currency_rate = column[Double]("currency_rate")
}

class PlatformDao extends BaseDao[Platforms](TableQuery[Platforms]) {

  def getPlatformById(id: String): Platform = {
    val query = tables.filter(_.id === id)

    val resultFuture = db.run(query.result)
    val rs           = Await.result(resultFuture, Duration.Inf)
    if(rs.isEmpty) null
    else rs.head
  }
}
