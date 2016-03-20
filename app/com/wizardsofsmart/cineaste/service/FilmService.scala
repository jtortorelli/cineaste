package com.wizardsofsmart.cineaste.service

import javax.inject.Inject

import com.wizardsofsmart.cineaste.domain.Film
import com.wizardsofsmart.cineaste.respository.FilmRepository
import com.wizardsofsmart.cineaste.value.error.{DomainError, EmptyResultsError}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class FilmService @Inject()(filmRepository: FilmRepository) {
   def films: Future[Either[DomainError, Seq[Film]]] = {
      filmRepository.films.map {
         case Right(response) =>
            val films = for (row <- Json.parse(response.body) \\ "row") yield row(0).as[Film]
            Right(films.sortWith(_.sortTitle < _.sortTitle))
         case Left(error) => Left(error)
      }
   }

   def film(uuid: String): Future[Either[DomainError, Film]] = {
      filmRepository.film(uuid).map {
         case Right(response) =>
            val films = for (row <- Json.parse(response.body) \\ "row") yield row(0).as[Film]
            if (films.nonEmpty) {
               Right(films.head)
            } else {
               Left(new EmptyResultsError)
            }
         case Left(error) => Left(error)
      }
   }

}
