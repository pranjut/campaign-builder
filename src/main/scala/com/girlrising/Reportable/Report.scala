package com.girlrising.Reportable

import com.girlrising.Models.Reporting._
import com.github.nscala_time.time.Imports._

trait Report {

	def values:List[DateStat]
	def name:String
	def numberUnit: String
	def amountUnit:Option[String]
	def color: Option[String] = None
	def dateUpdated: DateTime

	def toTimeseries(filter: Option[Any] = None): Timeseries = {
		Timeseries(values,name,numberUnit,amountUnit,color,dateUpdated)
	}

	def toValuesRickshaw(color: Option[String] = None)(filter: Option[Any] = None) = {
		this.toTimeseries(filter).valuesToRickshaw(color)
	}

	def toNumbersRickshaw(color: Option[String] = None)(filter: Option[Any] = None) = {
		this.toTimeseries(filter).numbersToRickshaw(color)
	}

	def toReportingStat(filter:Option[Any] = None) = {
		this.toTimeseries(filter).reportingStat
	}

}