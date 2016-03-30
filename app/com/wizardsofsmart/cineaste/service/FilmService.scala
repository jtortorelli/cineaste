package com.wizardsofsmart.cineaste.service

import javax.inject.Inject

import com.wizardsofsmart.cineaste.domain.film.{Film, FilmCast, FilmSeries, FilmStaff, FilmStudio}
import com.wizardsofsmart.cineaste.respository.FilmRepository
import com.wizardsofsmart.cineaste.value.error.{DomainError, EmptyResultsError}
import play.api.Play
import play.api.Play.current
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source


class FilmService @Inject()(filmRepository: FilmRepository) {
   def films: Future[Either[DomainError, Seq[Film]]] = {
      filmRepository.films.map {
         case Right(response) =>
            val films = for (row <- Json.parse(response.body) \\ "row") yield row(0).as[Film]
            Right(films.sortWith(_.sortTitle < _.sortTitle))
         case Left(error) => Left(error)
      }
   }

   def film(uuid: String): Future[Either[DomainError, (Film, Seq[FilmStaff], Seq[FilmCast], Seq[FilmStudio], Option[FilmSeries], String)]] = {
      filmRepository.film(uuid).map {
         case Right(response) =>
            val json = Json.parse(response.body) \\ "data"
            val films = for (r <- json(0) \\ "row") yield r(0).as[Film]
            val staff = for (r <- json(1) \\ "row") yield r(0).as[FilmStaff]
            val cast = for (r <- json(2) \\ "row") yield r(0).as[FilmCast]
            val studios = for (r <- json(3) \\ "row") yield r(0).as[FilmStudio]
            val seriesSeq: Seq[FilmSeries] = for (r <- json(4) \\ "row") yield r(0).as[FilmSeries]
            val series: Option[FilmSeries] = if (seriesSeq.nonEmpty) Some(seriesSeq(0)) else None
            val synopsis = getSynopsis(uuid)
            if (films.isEmpty || staff.isEmpty || cast.isEmpty || studios.isEmpty || synopsis.isEmpty) {
               Left(new EmptyResultsError)
            } else {
               Right((films(0), staff, cast, studios, series, synopsis.get))
            }
         case Left(error) => Left(error)
      }
   }

   private def getSynopsis(uuid: String): Option[String] = {
      val source = Play.getExistingFile(s"public/text/synopses/$uuid.txt")
      if (source.isDefined) {
         Some(Source.fromFile(source.get).getLines().toSeq.tail.mkString("<p>", "</p><p>", "</p>"))
      } else {
         None
      }
   }

}
