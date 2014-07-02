package com.girlrising.Models.Reporting

import com.github.nscala_time.time.Imports._

case class DateStat(date: DateTime, number: Int, amount: Option[Double])
