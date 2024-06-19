package com.flinters.etl.model

case class FileDataExtract(
  fileName:    String,
  reportDate:  String,
  account:     Account,
  platform:    Platform,
  fileHeaders: List[String],
  fileRows:    List[FileDataRow]
)

case class FileInfo(
  account:  Account,
  platform: Platform
)
