package com.girlrising.Models.Reporting

import com.github.nscala_time.time.Imports._

case class Timeseries(values:List[DateStat], name:String, numberUnit: String, amountUnit:Option[String], color: Option[String] = None, dateUpdated: DateTime){
	def numbersToRickshaw(color: Option[String] = None): Rickshaw = {
		Rickshaw.numbersToRickshaw(this,color)
	}
	def valuesToRickshaw(color: Option[String] = None): Rickshaw = {
		Rickshaw.valuesToRickshaw(this,color)
	}
	def reportingStat: ReportingStat = {
		ReportingStat.fromTimeseries(this)
	}
}