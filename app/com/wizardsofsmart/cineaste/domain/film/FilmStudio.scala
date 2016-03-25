package com.wizardsofsmart.cineaste.domain.film

import play.api.libs.json.{Reads, __}

case class FilmStudio(name: String)

object FilmStudio {
   implicit val filmStudioReads: Reads[FilmStudio] =
      (__ \ "name").read[String].map(FilmStudio.apply)
}
