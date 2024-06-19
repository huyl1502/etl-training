package com.flinters.etl.model

case class OutputFileRow(
  reportDate:   String,
  platformCode: String,
  platformName: String,
  accountId:    String,
  adAccountId:  Option[String],
  accountName:  String,
  imp:          Double,
  click:        Double,
  cost:         Double,
  cv:           Double
) {

  def getFields: scala.Seq[String] = this.productIterator.map {
    case v: Option[_] => v.getOrElse("").toString
    case v            => v.toString
  }.toSeq
}
