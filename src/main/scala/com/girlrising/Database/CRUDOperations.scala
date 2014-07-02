package com.girlrising.Database

import scala.concurrent.{ExecutionContext, Future,Await}
import ExecutionContext.Implicits.global
import reactivemongo.api.MongoDriver
import reactivemongo.core.nodeset.Authenticate
import reactivemongo.bson.BSONDocument
import scala.util.{Try, Success, Failure}
import com.girlrising.Models.Core._
import com.github.nscala_time.time.Imports._


class CRUDOperations[T](database: String)(implicit reader: BSONDocument => CoreModel[T], writer: CoreModel[T] => BSONDocument) extends MongoCore{

  //maybe add ability to increment, fold in pre-db call and post db-call events

  def add(value: CoreModel[T]) = {
    val value2 = value.createTimestamp
    AddToDatabase[CoreModel[T]](database,value2,List(IncId))
  }

  def edit(value: CoreModel[T], id: Int) = {
    val value2 = value.updateTimestamp
    val selector = BSONDocument("id" -> id)
    EditInDatabase[CoreModel[T]](database,selector,value2)
  }

  def remove(id: Int) = {
    RemoveFromDatabase(database,BSONDocument("id" -> id))
  }

  def all() = {
    AllFromDatabase[CoreModel[T]](database,BSONDocument())
  }

  def one(id: Int) = {
    val selector = BSONDocument("id" -> id)
    OneFromDatabase[CoreModel[T]](database,selector)
  }

}