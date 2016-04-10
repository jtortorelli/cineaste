package com.wizardsofsmart.cineaste.util

import java.text.{ParseException, SimpleDateFormat}

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object DateUtil {
   val completeFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-M-d")
   val yearAndMonthFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-M")
   val yearFormat: SimpleDateFormat = new SimpleDateFormat("yyyy")
   val completeFormatter = DateTimeFormat.forPattern("MMMM d, yyyy")
   val yearAndMonthFormatter = DateTimeFormat.forPattern("MMMM, yyyy")
   val yearFormatter = DateTimeFormat.forPattern("yyyy")


   def prettyPrintDate(date: String): Option[String] = {
      if (isCompleteDate(date)) {
         Some(completeFormatter.print(new DateTime(date)))
      } else if (isYearAndMonth(date)) {
         Some(yearAndMonthFormatter.print(new DateTime(date)))
      } else if (isYear(date)) {
         Some(yearFormatter.print(new DateTime(date)))
      } else {
         None
      }
   }

   private def isCompleteDate(date: String): Boolean = {
      try {
         completeFormat.parse(date)
         true
      } catch {
         case _: ParseException => false
      }
   }

   private def isYearAndMonth(date: String): Boolean = {
      try {
         yearAndMonthFormat.parse(date)
         true
      } catch {
         case _: ParseException => false
      }
   }

   private def isYear(date: String): Boolean = {
      try {
         yearFormat.parse(date)
         true
      } catch {
         case _: ParseException => false
      }
   }

}
