package com.girlrising.Map

import scala.xml.NodeSeq
import com.girlrising.Models.Aux._
import com.girlrising.Models.Aux.Map._

trait Mappable{

  def address: Option[Location] = None
  def name: Option[String] = None
  def description: Option[String] = None
  def data: Option[List[ExtendedData]] = None

  def toKML(withStyle: Option[Style]) = {
    val coords = address.flatMap(_.coordinates)
      coords match {
        case Some(c) =>
          Some(<Placemark>
            <name>{ name.getOrElse("") }</name>
            <styleUrl>{ withStyle.getOrElse("") }</styleUrl>
            <description>{ description.getOrElse("") }</description>
            <Point>
              <coordinates>{c.lng},{c.lat}</coordinates>
            </Point>
            <ExtendedData>{ data.map(_.map(_.toKML)) }</ExtendedData>
          </Placemark>)
        case _ => None
      }
    }

    def toKMLCenteredAt(withStyle: Option[Style], centered: Location) = {
      val coords = address.flatMap(_.coordinates)
      val centerCoords = centered.coordinates
      centerCoords match {
        case Some(center) => coords match {
          case Some(point) =>
            Some(<Placemark>
              <styleUrl>{ withStyle.getOrElse("") }</styleUrl>
              <LineString>
                <coordinates>
                  {point.lng},{point.lat}
                  {center.lng},{center.lat}
                </coordinates>
              </LineString>
            </Placemark>)
          case _ => None
        }
        case _ => None
      }
    }

    /*
    def EventsToKML(eventList: List[Event]):NodeSeq = {
      <kml xmlns="http://www.opengis.net/kml/2.2">
      <Document>
      {makeStyle()}
      {eventList.flatMap(event => KML.EventToKML(event))}
      </Document>
      </kml>;
    }
    */
}