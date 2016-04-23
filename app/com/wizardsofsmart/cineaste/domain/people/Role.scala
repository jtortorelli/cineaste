package com.wizardsofsmart.cineaste.domain.people

import com.wizardsofsmart.cineaste.domain.film.Film
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

sealed trait Role {
   val role: String
}

case class StaffRole(role: String, films: Seq[Film]) extends Role

object StaffRole {
   implicit val personStaffRoleReads: Reads[StaffRole] = (
         (JsPath \ "role").read[String] and
               (JsPath \ "films").read[Seq[Film]]
         ) (StaffRole.apply _)
}

case class CastCredit(film: Film, characters: Seq[String])

object CastCredit {
   implicit val personCastCreditReads: Reads[CastCredit] = (
         (JsPath \ "film").read[Film] and
               (JsPath \ "characters").read[Seq[String]]
         )(CastCredit.apply _)
}


case class CastRole(credits: Seq[CastCredit]) extends Role {
   val role = "Actor"
}
