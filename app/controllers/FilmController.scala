package controllers

import javax.inject.Inject

import com.wizardsofsmart.cineaste.service.FilmService
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}


class FilmController @Inject()(filmService: FilmService) extends Controller {

  def listFilms = Action.async {
    filmService.retrieveFilmsList.map { response =>
      Ok(Json.toJson(response))
    }
  }
}
