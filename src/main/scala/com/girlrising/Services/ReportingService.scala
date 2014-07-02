package com.girlrising.Services

import reactivemongo.bson.{BSONDocument,BSONDocumentReader,BSONDocumentWriter}
import spray.json._
import DefaultJsonProtocol._
import spray.routing.authentication.BasicAuth
import com.girlrising.Database._
import spray.routing.HttpService
import com.girlrising.Models.Core.CoreModel

/*
To follow conventions:
GET     /value?params
POST    /value
GET     /value/id
PUT     /value/id
DELETE  /value/id
*/

trait ReportingRoute[T] extends BasicRoute[T]{
  def ReportingRoute(implicit a: akka.actor.ActorRefFactory) = new ReportingService[T].route
}

class ReportingService[T](implicit database: String,
    a: akka.actor.ActorRefFactory,
    BSONreader: BSONDocumentReader[CoreModel[T]],
    BSONwriter: BSONDocumentWriter[CoreModel[T]],
    JSONWriter: JsonFormat[CoreModel[T]]) extends HttpService{
 
def actorRefFactory = a

implicit def read = BSONreader.read _
implicit def write = BSONwriter.write _

val controller = new CRUDOperations[T](database)

import scala.concurrent.ExecutionContext.Implicits.global
import spray.httpx.SprayJsonSupport._

val route =
    path(database){
      get{
        parameterMap {params =>
          complete{
            controller.all().map(_.map(_.toJson))
          }
        }
      } ~
      post{
        decompressRequest() {
          entity(as[String]) { value =>            // transfer to newly spawned actor
            detach() {
                complete{
                  controller.add(value.parseJson.convertTo[CoreModel[T]]).map(_.toJson.asJsObject)
                }
              }
            }
        }
      } ~ 
      options{
        complete{
          "option"
        }
      }
    } ~
    pathPrefix(database / IntNumber) { id =>
        get{
            complete{
              controller.one(id).map(_.toJson.asJsObject)
            }
        } ~
        options{
          complete{
            "option"
          }
        } ~
        put{
          entity(as[String]) { value =>
            complete{
              controller.edit(value.parseJson.convertTo[CoreModel[T]],id).map(_.toJson.asJsObject)
            }
          }
        } ~
        delete{
          complete{
            controller.remove(id)
          }
        }
      }
}