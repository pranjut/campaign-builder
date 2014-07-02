package com.girlrising.Services

import reactivemongo.bson.{BSONDocument,BSONDocumentReader,BSONDocumentWriter}
import spray.json._
import DefaultJsonProtocol._
import spray.routing.authentication.BasicAuth
import com.girlrising.Database._
import spray.routing.HttpService
import com.girlrising.Map._
import com.girlrising.Models.Core.CoreModel

/*
To follow conventions:
GET     /value/map.xml
*/

trait MapRoute[T] extends BasicRoute[T with Mappable] {
  def mapRoute(implicit a: akka.actor.ActorRefFactory) = new MapService[T].route
}

class MapService[T](implicit database: String,
    a: akka.actor.ActorRefFactory,
    BSONreader: BSONDocumentReader[CoreModel[T with Mappable]],
    BSONwriter: BSONDocumentWriter[CoreModel[T with Mappable]],
    JSONWriter: JsonFormat[CoreModel[T with Mappable]]) extends HttpService with Map with Mappable{

def actorRefFactory = a

implicit def read = BSONreader.read _
implicit def write = BSONwriter.write _

val controller = new CRUDOperations[T with Mappable](database)

import scala.concurrent.ExecutionContext.Implicits.global
import spray.httpx.SprayJsonSupport._
import DefaultJsonProtocol._

val route =
    pathPrefix(database){
      pathPrefix("map.xml"){
        parameterMap { params =>
          get{
            complete{
              controller.all().map(list => KML(list.map(_.model)))
            }
          }
        }
      }
    /* ~    
    pathPrefix(IntNumber){ id =>
      pathPrefix("map.xml"){
        get{
          complete{
            controller.one(id).map(_.toJson.asJsObject)
          }
        }
      }
    }*/
    }
}