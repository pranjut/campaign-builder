package com.girlrising.Models

import com.girlrising.Models.Core._
import com.girlrising.Models.Aux._
import com.girlrising.CoreJSONProtocol._

import spray.json._

object JSONProtocol extends DefaultJsonProtocol {
	implicit val coordinateFormat = jsonFormat2(Coordinate.apply)
	implicit val locationFormat = jsonFormat7(Location.apply)
	implicit val donationFormat = jsonFormat3(Donation.apply)
	implicit val donationModelFormat = jsonFormat4(CoreModel[Donation])
}