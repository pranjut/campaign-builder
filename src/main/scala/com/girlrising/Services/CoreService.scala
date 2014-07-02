package com.girlrising.Services

import reactivemongo.bson.{BSONDocument,BSONDocumentReader,BSONDocumentWriter}

import com.girlrising.Database._
import com.girlrising.Models.Core.CoreModel

import spray.json._
import spray.routing._
import spray.http._

trait BasicHttpService extends HttpService

trait BasicRoute[T] {
	implicit def database: String
	implicit def BSONreader: BSONDocumentReader[CoreModel[T]]
  implicit def BSONwriter: BSONDocumentWriter[CoreModel[T]]
  //implicit def BSONReader: BSONDocument => T
  //implicit def BSONWriter: T => BSONDocument
  implicit def JSONWriter: RootJsonFormat[CoreModel[T]]
}