package com.wizardsofsmart.cineaste.domain.people

import com.wizardsofsmart.cineaste.domain.film.Film
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

sealed trait Role {
   val role: String
}

case class PersonStaffRole(role: String, films: Seq[Film]) extends Role

object PersonStaffRole {
   implicit val personStaffRoleReads: Reads[PersonStaffRole] = (
         (JsPath \ "role").read[String] and
               (JsPath \ "films").read[Seq[Film]]
         ) (PersonStaffRole.apply _)
}

case class PersonCastCredit(film: Film, characters: Seq[String])

object PersonCastCredit {
   implicit val personCastCreditReads: Reads[PersonCastCredit] = (
         (JsPath \ "film").read[Film] and
               (JsPath \ "characters").read[Seq[String]]
         )(PersonCastCredit.apply _)
}


case class PersonCastRole(credits: Seq[PersonCastCredit]) extends Role {
   val role = "Actor"
}
