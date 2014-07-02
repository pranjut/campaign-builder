package com.girlrising.Models.Aux

import spray.json._

import reactivemongo.bson._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

case class Location(street1: Option[String] = None, street2: Option[String] = None, city: Option[String] = None, state: Option[String] = None, country: Option[String] = None, zipPostal: Option[String] = None, coordinates: Option[Coordinate] = None){
  def addCoordinate(newCoordinates: Option[Coordinate]): Location = {
    this.copy(coordinates = newCoordinates)
  }
}

object Location{

  /*
  def FindCoordinates(Location: Location): Future[Location] = {
    Boot.geo.geocodeLocation(Location).map(coords => Location.addCoordinate(coords))
  }
  */

  implicit object LocationReader extends BSONDocumentReader[Location] {

    def read(doc: BSONDocument) = {

      Location(
        doc.getAs[String]("street1"),
        doc.getAs[String]("street2"),
        doc.getAs[String]("city"),
        doc.getAs[String]("state"),
        doc.getAs[String]("country"),
        doc.getAs[String]("zipPostal"),
        doc.getAs[Coordinate]("coordinates")
      )

    }

  }

  implicit object LocationWriter extends BSONDocumentWriter[Location] {

    def write(Location: Location) = {

      BSONDocument(
        "Location1"      -> Location.street1,
        "Location2"      -> Location.street2,
        "city"          -> Location.city,
        "state"         -> Location.state,
        "country"       -> Location.country,
        "zipPostal"     -> Location.zipPostal,
        "coordinates"   -> Location.coordinates.map(coordinates => Coordinate.CoordinateWriter.write(coordinates))
      )

    }

  }

}