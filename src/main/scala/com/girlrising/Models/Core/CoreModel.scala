package com.girlrising.Models.Core

import reactivemongo.bson.{BSONDocument, BSONDateTime}
import com.github.nscala_time.time.Imports._

case class CoreModel[T](dateCreated: Option[DateTime] = None, dateUpdated: Option[DateTime] = None, id: Option[Int] = None, model: T){
	def createTimestamp = this.copy(dateCreated = Some(DateTime.now), dateUpdated = Some(DateTime.now))
	def updateTimestamp = this.copy(dateUpdated = Some(DateTime.now))
}

object Core {
	def Writer[T](subWrite: T => BSONDocument, name: String, value: CoreModel[T]): BSONDocument = {
		BSONDocument(
			"dateCreated" -> value.dateCreated.map(time => BSONDateTime(time.millis)),
			"dateUpdated" -> value.dateUpdated.map(time => BSONDateTime(time.millis)),
			"id" -> value.id,
			name -> subWrite(value.model)
		)
	}
	def Reader[T](subRead: BSONDocument => T, name: String, doc: BSONDocument): CoreModel[T] = {
		CoreModel[T](
			doc.getAs[BSONDateTime]("dateCreated").map(dt => new DateTime(dt.value)),
			doc.getAs[BSONDateTime]("dateUpdated").map(dt => new DateTime(dt.value)),
			doc.getAs[Int]("id"),
			doc.getAs[BSONDocument](name).map(subRead(_)).get
		)
	}
}