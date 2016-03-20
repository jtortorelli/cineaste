package controllers

import javax.inject.Inject

import com.wizardsofsmart.cineaste.service.FilmService
import com.wizardsofsmart.cineaste.value.error.{EmptyResultsError, Neo4jConnectionError}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global


class FilmController @Inject()(filmService: FilmService) extends Controller {

   def films = Action.async {
      filmService.films.map {
         case Right(response) => Ok(views.html.film.films(response))
         case Left(error) => error match {
            case _: Neo4jConnectionError => ServiceUnavailable("Could not connect to database")
            case _ => InternalServerError
         }
      }
   }

   def film(uuid: String) = Action.async {
      filmService.film(uuid).map {
         case Right(response) => Ok(views.html.film.film(response))
         case Left(error) => error match {
            case _: Neo4jConnectionError => ServiceUnavailable("Could not connect to database")
            case _: EmptyResultsError => NotFound("Query returned no results")
            case _ => InternalServerError
         }
      }
   }

}
