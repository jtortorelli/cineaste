package com.wizardsofsmart.cineaste.domain

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

sealed trait People

case class Group(uuid: String, name: String) extends People

case class Person(uuid: String,
                  firstName: String,
                  lastName: String,
                  japaneseName: Option[String],
                  birthName: Option[String],
                  dob: Option[String],
                  dod: Option[String],
                  aliases: Option[Seq[String]],
                  birthPlace: Option[String],
                  deathPlace: Option[String],
                  showcase: Boolean) extends People

object Person {
   implicit val personReads: Reads[Person] = (
         (JsPath \ "uuid").read[String] and
               (JsPath \ "first_name").read[String] and
               (JsPath \ "last_name").read[String] and
               (JsPath \ "japanese_name").readNullable[String] and
               (JsPath \ "birth_name").readNullable[String] and
               (JsPath \ "dob").readNullable[String] and
               (JsPath \ "dod").readNullable[String] and
               (JsPath \ "aliases").readNullable[Seq[String]] and
               (JsPath \ "birth_place").readNullable[String] and
               (JsPath \ "death_place").readNullable[String] and
               (JsPath \ "showcase").read[Boolean]
         ) (Person.apply _)

}