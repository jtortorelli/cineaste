package com.wizardsofsmart.cineaste.domain.film

import java.util.Date

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

case class Film(uuid: String,
                japaneseTitleTransliteration: Option[String],
                showcase: Boolean,
                aliases: Option[Seq[String]],
                title: String,
                japaneseTitleTranslation: Option[String],
                japaneseTitleUnicode: Option[String],
                releaseDate: Date,
                duration: Int) {
   val releaseYear = new DateTime(this.releaseDate).getYear

   val displayReleaseDate = {
      val formatter = DateTimeFormat.forPattern("MMMM d, yyyy")
      formatter.print(new DateTime(this.releaseDate))
   }

   val sortTitle = {
      if (this.title.startsWith("The ")) {
         "The ".r replaceFirstIn(this.title, "")
      } else {
         this.title
      }
   }
}

object Film {
   implicit val filmReads: Reads[Film] = (
         (JsPath \ "uuid").read[String] and
               (JsPath \ "jt_transliteration").readNullable[String] and
               (JsPath \ "showcase").read[Boolean] and
               (JsPath \ "aliases").readNullable[Seq[String]] and
               (JsPath \ "title").read[String] and
               (JsPath \ "jt_translation").readNullable[String] and
               (JsPath \ "jt_unicode").readNullable[String] and
               (JsPath \ "release_date").read[Date] and
               (JsPath \ "duration").read[Int]
         ) (Film.apply _)


   implicit val filmWrites = new Writes[Film] {
      def writes(film: Film) = Json.obj(
         "uuid" -> film.uuid,
         "jt_transliteration" -> film.japaneseTitleTransliteration,
         "showcase" -> film.showcase,
         "aliases" -> film.aliases,
         "title" -> film.title,
         "jt_translation" -> film.japaneseTitleTranslation,
         "jt_unicode" -> film.japaneseTitleUnicode,
         "release_date" -> film.releaseDate,
         "duration" -> film.duration
      )
   }

}