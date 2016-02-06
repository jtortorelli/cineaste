package com.wizardsofsmart.cineaste.domain

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

case class Film(
                 key: String,
                 japaneseTitleTransliteration: Option[String],
                 display: String,
                 aliases: Option[Seq[String]],
                 title: String,
                 japaneseTitleTranslation: Option[String],
                 japaneseTitleUnicode: Option[String],
                 releaseDate: String,
                 duration: String
               )

object Film {
  implicit val filmReads: Reads[Film] = (
    (JsPath \ "key").read[String] and
      (JsPath \ "jt_transliteration").readNullable[String] and
      (JsPath \ "display").read[String] and
      (JsPath \ "aliases").readNullable[Seq[String]] and
      (JsPath \ "title").read[String] and
      (JsPath \ "jt_translation").readNullable[String] and
      (JsPath \ "jt_unicode").readNullable[String] and
      (JsPath \ "release_date").read[String] and
      (JsPath \ "duration").read[String]
    ) (Film.apply _)


  implicit val filmWrites = new Writes[Film] {
    def writes(film: Film) = Json.obj(
      "key" -> film.key,
      "jt_transliteration" -> film.japaneseTitleTransliteration,
      "display" -> film.display,
      "aliases" -> film.aliases,
      "title" -> film.title,
      "jt_translation" -> film.japaneseTitleTranslation,
      "jt_unicode" -> film.japaneseTitleUnicode,
      "release_date" -> film.releaseDate,
      "duration" -> film.duration
    )
  }
}