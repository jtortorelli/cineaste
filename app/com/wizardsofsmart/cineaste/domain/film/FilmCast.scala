package com.wizardsofsmart.cineaste.domain.film

import com.wizardsofsmart.cineaste.domain.people.People
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

case class FilmCast(people: People, characters: Seq[String], orders: Seq[Int])

object FilmCast {
   implicit val filmCastReads: Reads[FilmCast] = (
         (JsPath \ "people").read[People] and
               (JsPath \ "characters").read[Seq[String]] and
               (JsPath \ "orders").read[Seq[Int]]
         ) (FilmCast.apply _)
}