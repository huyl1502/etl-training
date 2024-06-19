package com.flinters.etl.model

case class FileDataTransform(
  fileHeaders: List[String],
  fileRows:    List[List[String]]
)
