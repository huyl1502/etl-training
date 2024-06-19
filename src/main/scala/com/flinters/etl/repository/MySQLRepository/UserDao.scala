package com.flinters.etl.repository.MySQLRepository

import com.flinters.etl.model.User
import com.flinters.etl.repository.MySQLRepository.DBConnection.db
import slick.lifted.{TableQuery, Tag}
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.Future

class Users(tag: Tag) extends Table[User](tag, "user") {

  def id          = column[Int]("id", O.PrimaryKey, O.AutoInc)
  private def age = column[Int]("age")

  def * = (id, age) <> (User.tupled, User.unapply)
}

class UserDao extends BaseDao[Users](TableQuery[Users]) {
  def getUsers: Future[scala.Seq[User]] = db.run(tables.result)
}
