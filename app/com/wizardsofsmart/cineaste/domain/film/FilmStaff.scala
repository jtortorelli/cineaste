package com.wizardsofsmart.cineaste.domain.film

import com.wizardsofsmart.cineaste.domain.{People, Person}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}


case class FilmStaff(role: String, people: Seq[People], orders: Seq[Int])

object FilmStaff {
   implicit val filmStaffReads: Reads[FilmStaff] = (
         (JsPath \ "role").read[String] and
               (JsPath \ "people").read[Seq[People]] and
               (JsPath \ "orders").read[Seq[Int]]
         ) (FilmStaff.apply _)
}
