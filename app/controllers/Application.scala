package controllers

import javax.inject.Inject

import com.wizardsofsmart.cineaste.service.FilmService
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._


class Application @Inject()(filmService: FilmService) extends Controller {

  def index = Action.async {
    val result = filmService.retrieveFilmsList
    result.map { response =>
      Ok(response.toString())
    }
  }

}
