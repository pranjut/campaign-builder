package com.girlrising.Models.Aux

import spray.json._

import reactivemongo.bson.{BSONDocument,BSONDocumentReader,BSONDocumentWriter}

case class Coordinate(lat: Double, lng: Double)

object Coordinate{

  implicit object CoordinateReader extends BSONDocumentReader[Coordinate] {

    def read(doc: BSONDocument) = {

      Coordinate(
        doc.getAs[Double]("lat").get,
        doc.getAs[Double]("lng").get
      )

    }

  }

  implicit object CoordinateWriter extends BSONDocumentWriter[Coordinate] {

    def write(coordinates: Coordinate) = {

      BSONDocument(
        "lat" -> coordinates.lat,
        "lng" -> coordinates.lng
      )

    }

  }

}