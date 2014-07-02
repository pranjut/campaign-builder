package com.girlrising.Database

import scala.concurrent.{ExecutionContext, Future,Await}
import ExecutionContext.Implicits.global
import reactivemongo.api.MongoDriver
import reactivemongo.core.nodeset.Authenticate
import reactivemongo.bson.BSONDocument
import reactivemongo.core.commands.LastError
import scala.concurrent.duration._
import scala.util.{Try, Success, Failure}
import com.girlrising.Exceptions._


trait MongoCore{

  val driver = new MongoDriver
  val connection = driver.connection(Seq("ds053858.mongolab.com:53858"),Seq(Authenticate("girlrising","ben","Loquat55")))
  val db = connection("girlrising")

  //get one object from database, if two ?

  //define enter/exit into database

  def OneFromDatabase[T](database: String, selector: BSONDocument, orElse: Option[T] = None)(implicit converter: BSONDocument => T): Future[T] = {
      db(database)
      .find(selector)
      .cursor
      .headOption
      .map(value => value match {
        case Some(result) => converter(result)
        case None => orElse match {
          case Some(other) => other
          case None => throw NotFoundException()
          }
        }
      ) recover {
        case e => throw DatabaseException()
      }
  }
  
  //get list of all objects from database, if none, just empty list
  def AllFromDatabase[T](database: String, selector: BSONDocument)(implicit converter: BSONDocument => T): Future[List[T]] = {
      db(database)
      .find(selector)
      .cursor
      .collect[List]()
      .map(value => 
        value.map(value =>
          converter(value)))
  }

  def AddToDatabase[T](database: String, value: T, MongoActions: List[(String,T) => Future[BSONDocument]] = List(NullMongoAction _))(implicit converter: T => BSONDocument, convertBack: BSONDocument => T): Future[T] = {
      val toadd = MongoActions
        .foldLeft(Future(converter(value)))((futureval,action) =>
        action(database,value)
          .flatMap(actionvalue =>
          futureval.map(futureval =>
            actionvalue ++ futureval)))
      toadd.flatMap(newvalue => db(database).insert(newvalue)).flatMap(
        _ => toadd.map(newvalue => convertBack(newvalue)) //onSuccess
      ).recover {
        case LastError(_,_,Some(11000),_,_,_,_) => throw DuplicateKeyException()
        case _ => throw DatabaseException()
      }
  }

  def NullMongoAction[T](string:String,value:T): Future[BSONDocument] = {
    Future(BSONDocument())
  }

  def AddFutureToDatabase[T](database: String, value: Future[T], MongoActions: List[(String,T) => Future[BSONDocument]] = List(NullMongoAction _))(implicit converter: T => BSONDocument, convertBack: BSONDocument => T): Future[T] = {
    value.flatMap(value =>
      AddToDatabase[T](database,value,MongoActions)
    )
  }

  def RemoveFromDatabase(database: String, selector: BSONDocument): Future[String] = {
      db(database).remove(selector).map(
        _ => "success"
      ) recover {
        case _ => "Not found"
      }
  }

  def EditInDatabase[T](database: String, selector: BSONDocument, value: T, BSONmod: Option[BSONDocument]=None)(implicit conversion: T => BSONDocument, convertBack: BSONDocument => T): Future[T] = {
      BSONmod match {
        case Some(modifier) => db(database).update(selector,modifier).map(_ => value)
        case None => db(database).update(selector,BSONDocument("$set" -> conversion(value))).flatMap(
          _ => OneFromDatabase[T](database,selector,None)
        )
      }
      /*.recover{
        case _ => throw TBDException("TBD")
      }*/
  }

  def IncId(col: String, value: Any): Future[BSONDocument] = {
    val selector = BSONDocument("_id" -> col)
    val modifier = BSONDocument("$inc" -> BSONDocument("seq" -> 1))
    val dataupdate = EditInDatabase("counters",selector,modifier,Some(modifier))
    dataupdate.flatMap{
      case _ => {
        Count(col).map(count => BSONDocument("id" -> count))
      }
    }
  }

  def Count(col:String): Future[Int] = {
    var selector = BSONDocument("_id" -> col)
    OneFromDatabase[BSONDocument]("counters",selector).map(count => count.getAs[Int]("seq").getOrElse(1))
  }

}