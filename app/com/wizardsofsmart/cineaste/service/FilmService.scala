package com.wizardsofsmart.cineaste.service

import javax.inject.Inject

import com.wizardsofsmart.cineaste.domain.{Film, FilmCast, FilmStaff}
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

   def film(uuid: String): Future[Either[DomainError, (Film, Seq[FilmStaff], Seq[FilmCast])]] = {
      filmRepository.film(uuid).map {
         case Right(response) =>
            val json = Json.parse(response.body) \\ "data"
            val films = for (r <- json(0) \\ "row") yield r(0).as[Film]
            val staff = for (r <- json(1) \\ "row") yield r(0).as[FilmStaff]
            val cast = for (r <- json(2) \\ "row") yield r(0).as[FilmCast]
            if (films.isEmpty || staff.isEmpty || cast.isEmpty) {
               Left(new EmptyResultsError)
            } else {
               Right((films(0), staff, cast))
            }
         case Left(error) => Left(error)
      }
   }

}
