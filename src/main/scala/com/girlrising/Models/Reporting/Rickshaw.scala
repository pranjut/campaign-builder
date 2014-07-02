package com.girlrising.Models.Reporting

import com.github.nscala_time.time.Imports._

case class RickshawPoint(x: Long, y:Double)

case class Rickshaw(name: String, data: List[RickshawPoint], color: String = "blue")

object Rickshaw{
	def toRickshaw(numbers: Boolean)(timeseries: Timeseries, color: Option[String]): Rickshaw = {
		val finalColor = color match {
			case Some(c) => c
			case None => timeseries.color match {
				case Some(c2) => c2
				case None => "blue"
			}
		}
		val data = numbers match {
			case false => timeseries.values.sortBy(_.date).map(point => RickshawPoint(point.date.millis/1000,point.amount.getOrElse(0.0)))
			case true => timeseries.values.sortBy(_.date).map(point => RickshawPoint(point.date.millis/1000,point.number))
		}
		Rickshaw(timeseries.name,data,finalColor)
	}
	def numbersToRickshaw(timeseries: Timeseries, color: Option[String]): Rickshaw = toRickshaw(true)(timeseries,color)
	def valuesToRickshaw(timeseries: Timeseries, color: Option[String]): Rickshaw = toRickshaw(false)(timeseries,color)
}