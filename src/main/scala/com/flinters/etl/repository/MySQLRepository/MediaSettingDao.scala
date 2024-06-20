package com.flinters.etl.repository.MySQLRepository

import com.flinters.etl.model.MediaSetting
import com.flinters.etl.repository.MySQLRepository.DBConnection.db
import slick.lifted.{ TableQuery, Tag }
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class MediaSettings(tag: Tag) extends Table[MediaSetting](tag, "media_settings") {

  def platform_id     = column[String]("platform_id", O.PrimaryKey, O.Unique)
  def imp_col         = column[String]("imp_col")
  def click_col       = column[String]("click_col")
  def cost_col        = column[String]("cost_col")
  def cv_col          = column[String]("cv_col")
  def report_date_col = column[String]("report_date_col")

  def * =
    (
      platform_id,
      imp_col,
      click_col,
      cost_col,
      cv_col,
      report_date_col
    ) <> ((MediaSetting.apply _).tupled, MediaSetting.unapply)
}

class MediaSettingDao extends BaseDao[MediaSettings](TableQuery[MediaSettings]) {

  def getMediaSettingById(id: String) = {
    val query = tables.filter(_.platform_id === id)

    val resultFuture = db.run(query.result)
    val rs           = Await.result(resultFuture, Duration.Inf).head
    rs
  }
}
