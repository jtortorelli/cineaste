package controllers

import javax.inject.Inject

import com.wizardsofsmart.cineaste.service.FilmService
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global


class FilmController @Inject()(filmService: FilmService) extends Controller {

   def films = Action.async {
      filmService.films.map {
         case Right(response) => Ok(views.html.films(response))
         case Left(error) => Ok(error)
      }
   }

   def film(uuid: String) = Action {
      Ok(filmService.film(uuid))
   }

}
