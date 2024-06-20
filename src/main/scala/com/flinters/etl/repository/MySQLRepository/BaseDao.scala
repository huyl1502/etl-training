package com.flinters.etl.repository.MySQLRepository

import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery

abstract class BaseDao[T <: Table[_]](tableQuery: TableQuery[T]) {
  val tables = tableQuery
}
