package com.wizardsofsmart.cineaste.domain.film

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}


case class FilmSeries(precedent: Option[Film], antecedent: Option[Film])

object FilmSeries {
   implicit val filmSeriesReads: Reads[FilmSeries] = (
         (JsPath \ "precedent").readNullable[Film] and
               (JsPath \ "antecedent").readNullable[Film]
         ) (FilmSeries.apply _)
}