package com.flinters.etl.constant

object Constant {

  val report_date   = "report_date"
  val platform_code = "platform_code"
  val platform_name = "platform_name"
  val account_id    = "account_id"
  val ad_account_id = "ad_account_id"
  val account_name  = "account_name"
  val imp           = "imp"
  val click         = "click"
  val cost          = "cost"
  val cv            = "cv"

  val OUTPUT_FILE_HEADERS: List[String] = List(
    report_date,
    platform_code,
    platform_name,
    account_id,
    ad_account_id,
    account_name,
    imp,
    click,
    cost,
    cv
  )
}
