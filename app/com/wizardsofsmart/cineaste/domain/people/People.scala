package com.wizardsofsmart.cineaste.domain.people

import org.joda.time.{DateTime, Period}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads, __}

sealed trait People {
   val displayName: String
   val sortName: String
}

object People {
   implicit val peopleReads: Reads[People] = {
      __.read[Person].map(x => x: People) orElse __.read[Group].map(x => x: People)
   }
}

case class Group(uuid: String, name: String, showcase: Boolean) extends People {
   override val displayName: String = this.name
   override val sortName: String = {
      if (this.name.startsWith("The ")) {
         "The ".r replaceFirstIn(this.name, "")
      } else {
         this.name
      }
   }
}

object Group {
   implicit val groupReads: Reads[Group] = (
         (JsPath \ "uuid").read[String] and
               (JsPath \ "name").read[String] and
               (JsPath \ "showcase").read[Boolean]
         ) (Group.apply _)
}

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
                  showcase: Boolean) extends People {
   override val displayName: String = s"${this.firstName} ${this.lastName}"
   override val sortName: String = s"${this.lastName}, ${this.firstName}"

   val age: Option[Int] = {
      if (dob.isDefined && dob.get != "unknown") {
         if (dod.isEmpty) {
            try {
               val jodaDob = new DateTime(dob.get)
               val currentDate = new DateTime()
               val period = new Period(jodaDob, currentDate)
               Some(period.getYears)
            } catch {
               case _: Throwable => None
            }
         } else if (dod.isDefined && dod.get != "unknown") {
            try {
               val jodaDob = new DateTime(dob.get)
               val jodaDod = new DateTime(dod.get)
               val period = new Period(jodaDob, jodaDod)
               Some(period.getYears)
            } catch {
               case _: Throwable => None
            }
         } else {
            None
         }
      } else {
         None
      }
   }

}

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