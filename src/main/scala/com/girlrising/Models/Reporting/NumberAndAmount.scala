package com.girlrising.Models.Reporting

case class NumberAndAmount(number: Int, amount: Option[Double]){
	def add(number: Int, amount: Option[Double]): NumberAndAmount = {
		this.amount match {
			case Some(oldAmount) => NumberAndAmount(this.number + number, Some(oldAmount + amount.getOrElse(0.0)))
			case None => NumberAndAmount(this.number + number, None)
		}
	}
}