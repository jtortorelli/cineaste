package com.wizardsofsmart.cineaste.domain.people

import com.wizardsofsmart.cineaste.domain.film.Film
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}


case class PersonStaffRole(role: String, films: Seq[Film])

object PersonStaffRole {
   implicit val personStaffRoleReads: Reads[PersonStaffRole] = (
         (JsPath \ "role").read[String] and
               (JsPath \ "films").read[Seq[Film]]
         )(PersonStaffRole.apply _)
}