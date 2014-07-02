package com.girlrising.Map

import scala.xml.NodeSeq
import com.girlrising.Models.Aux._
import com.girlrising.Models.Aux.Map._

trait Map{
 
	val style: Option[Style] = None;

	def KML[T](points: List[T with Mappable]) = {
		KMLwithStyle[T](points,style)
	}

  def KMLwithStyle[T](points:List[T with Mappable], style: Option[Style] = None) = {
     <kml xmlns="http://www.opengis.net/kml/2.2">
      <Document>
      {style.map(_.id)}
      {points.flatMap(point => point.toKML(style))}
      </Document>
     </kml>;
  }

  def KMLFromPoint[T](center: T with Mappable, points: List[T with Mappable], style: Option[Style] = None) = {
    val centerLocation = center.address
    centerLocation match{
      case Some(centerAddress) =>
      <kml xmlns="http://www.opengis.net/kml/2.2">
        <Document>
        {style.map(_.id)}
        {points.flatMap(point => point.toKMLCenteredAt(style,centerAddress ))}
        </Document>
       </kml>;
      case _ => None
    }
  }

}