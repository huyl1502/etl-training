package com.flinters.etl.repository.MySQLRepository

import com.flinters.etl.model.ActionLog
import com.flinters.etl.repository.MySQLRepository.DBConnection.db
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{TableQuery, Tag}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ActionLogs(tag: Tag) extends Table[ActionLog](tag, "action_log") {

  def * =
    (
      id.?,
      fileName,
      result,
      executionTime
    ) <> ((ActionLog.apply _).tupled, ActionLog.unapply)

  def id            = column[Int]("id", O.PrimaryKey, O.Unique)

  def fileName          = column[String]("file_name")

  def result = column[String]("etl_result")

  def executionTime = column[String]("execution_time")
}

class ActionLogDao extends BaseDao[ActionLogs](TableQuery[ActionLogs]) {
  def insertActionLog(actionLog: ActionLog) = {
    val insertAction = tables returning tables.map(_.id) into ((log, id) => log.copy(id = Some(id))) += actionLog
    val insertResult = db.run(insertAction)
    val insertedLog = Await.result(insertResult, Duration.Inf)
    db.close()
  }
}
