package com.wizardsofsmart.cineaste.service

import javax.inject.Inject

import com.wizardsofsmart.cineaste.domain.Film
import com.wizardsofsmart.cineaste.respository.FilmRepository
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global


class FilmService @Inject()(filmRepository: FilmRepository) {
   def films = {
      filmRepository.films.map {
         response =>
            val films = for (row <- Json.parse(response.body) \\ "row") yield row(0).as[Film]
            films.sortWith(_.sortTitle < _.sortTitle)
      }
   }

}
