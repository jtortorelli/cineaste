package com.wizardsofsmart.cineaste.domain.film

import com.wizardsofsmart.cineaste.domain.Person
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}


case class FilmStaff(role: String, people: Seq[Person], orders: Seq[Int])

object FilmStaff {
   implicit val filmStaffReads: Reads[FilmStaff] = (
         (JsPath \ "role").read[String] and
               (JsPath \ "people").read[Seq[Person]] and
               (JsPath \ "orders").read[Seq[Int]]
         ) (FilmStaff.apply _)
}
