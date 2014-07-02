package com.girlrising.Models.Core

import com.girlrising.Services.{BasicHttpService,CRUDRoute,MapRoute,ReportingRoute}

import reactivemongo.bson.{BSONDocumentWriter, BSONDocumentReader, BSONDocument, BSONDateTime}
import com.github.nscala_time.time.Imports._
import org.joda.time.Days
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import spray.json._
import com.girlrising.Map._
import com.girlrising.Models.Aux._
import com.girlrising.Models.JSONProtocol._

case class Donation(amount: Double, date: DateTime, location: Option[Location]) extends Mappable { override def address = location }

object Donation extends CRUDRoute[Donation] with MapRoute[Donation] with ReportingRoute[Donation]{

	def database = "donation"
	def JSONWriter = donationModelFormat

	trait DonationService extends BasicHttpService{
		val donationRoute = CRUDRoute ~ mapRoute
	}

	implicit object BSONreader extends BSONDocumentReader[CoreModel[Donation]]{
		def subRead(doc: BSONDocument): Donation = {
			Donation(
				doc.getAs[Double]("amount").get,
				doc.getAs[BSONDateTime]("date").map(dt => new DateTime(dt.value)).get,
				doc.getAs[Location]("location")
			)
		}
		def read(doc: BSONDocument): CoreModel[Donation] = Core.Reader[Donation](subRead, database, doc)
		/*def read(doc: BSONDocument): CoreModel[Donation]{
			Core.Read(doc,subRead,database)
		}*/
	}

	implicit object BSONwriter extends BSONDocumentWriter[CoreModel[Donation]]{
		def subWrite(donation: Donation): BSONDocument = {
			BSONDocument(
				"amount" -> donation.amount,
				"date" -> BSONDateTime(donation.date.millis),
				"location" -> donation.location.map(Location.LocationWriter.write(_))
			)
		}
		def write(donation: CoreModel[Donation]): BSONDocument = Core.Writer[Donation](subWrite, database, donation)

	}

}