package com.wizardsofsmart.cineaste.domain

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

case class Person(
                       uuid: String,
                       firstName: String,
                       lastName: String,
                       japaneseName: Option[String],
                       birthName: Option[String],
                       dob: Option[String],
                       dod: Option[String],
                       aliases: Option[Seq[String]],
                       birthPlace: Option[String],
                       deathPlace: Option[String],
                       showcase: Boolean
                 )

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

   implicit val personWrites = new Writes[Person] {
      def writes(person: Person) = Json.obj(
         "uuid" -> person.uuid,
         "first_name" -> person.firstName,
         "last_name" -> person.lastName,
         "japanese_name" -> person.japaneseName,
         "birth_name" -> person.birthName,
         "dob" -> person.dob,
         "dod" -> person.dod,
         "aliases" -> person.aliases,
         "birth_place" -> person.birthPlace,
         "death_place" -> person.deathPlace,
         "showcase" -> person.showcase
      )
   }
}