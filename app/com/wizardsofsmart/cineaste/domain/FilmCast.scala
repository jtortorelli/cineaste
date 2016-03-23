package com.wizardsofsmart.cineaste.domain

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

case class FilmCast(person: Person, characters: Seq[String], orders: Seq[Int])

object FilmCast {
   implicit val filmCastReads: Reads[FilmCast] = (
         (JsPath \ "person").read[Person] and
               (JsPath \ "characters").read[Seq[String]] and
               (JsPath \ "orders").read[Seq[Int]]
         ) (FilmCast.apply _)
}