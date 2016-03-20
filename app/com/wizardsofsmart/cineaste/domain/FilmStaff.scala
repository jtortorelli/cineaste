package com.wizardsofsmart.cineaste.domain

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}


case class FilmStaff(role: String, names: Seq[String], orders: Seq[Int])

object FilmStaff {
   implicit val filmStaffReads: Reads[FilmStaff] = (
         (JsPath \ "role").read[String] and
               (JsPath \ "names").read[Seq[String]] and
               (JsPath \ "orders").read[Seq[Int]]
         ) (FilmStaff.apply _)
}
