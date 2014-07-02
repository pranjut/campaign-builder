package com.girlrising.Models.Reporting

import com.github.nscala_time.time.Imports._

case class ReportingStat(dateUpdated: DateTime, name: String, year: NumberAndAmount, month: NumberAndAmount, week: NumberAndAmount, amountUnit: Option[String], numberUnit: String)

object ReportingStat{
	def fromTimeseries(timeseries: Timeseries): ReportingStat = {
		val thisYear = DateTime.now.month(1).day(1).hour(0).minute(0).second(0)
		val thisMonth = DateTime.now.day(1).hour(0).minute(0).second(0)
		val thisWeek = DateTime.now.minusDays(DateTime.now.getDayOfWeek()).hour(0).minute(0).second(0)
		def total(period: DateTime): NumberAndAmount = {
			val start = timeseries.values.head.amount match {
				case Some(amount) => NumberAndAmount(0,Some(0.0))
				case None => NumberAndAmount(0,None)
			}
			timeseries.values.filter(_.date >= period.minusDays(1)).foldLeft(start)((total,toAdd) => total.add(toAdd.number,toAdd.amount))
		}
		val totalYear = total(thisYear)
		val totalMonth = total(thisMonth)
		val totalWeek = total(thisWeek)
		ReportingStat(timeseries.dateUpdated,timeseries.name,totalYear,totalMonth,totalWeek,timeseries.amountUnit,timeseries.numberUnit)
	}
}