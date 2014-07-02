package com.girlrising

import reactivemongo.bson._
import com.github.nscala_time.time.Imports._
import spray.json._
import org.joda.time.format.{ISODateTimeFormat, DateTimeFormatter}

object CoreJSONProtocol extends DefaultJsonProtocol {

  implicit object DateTimeJsonFormat extends RootJsonFormat[DateTime] {
    //different datetime options
    //val dateTime = """\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d:[0-5]\d\.\d+([+-][0-2]\d:[0-5]\d|Z)""".r
    val fmt: DateTimeFormatter = ISODateTimeFormat.dateTimeParser()
    val fmt_print: DateTimeFormatter = ISODateTimeFormat.dateTime()

    def write(d: DateTime) =
      JsString(fmt_print.print(d))

    def read(value: JsValue) = value match {
      case JsString(string) => fmt.parseDateTime(string)
      case _ => DateTime.now //handle improperly formatted information
    }

  }
}