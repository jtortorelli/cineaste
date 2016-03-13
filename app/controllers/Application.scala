package controllers

import javax.inject.Inject

import com.wizardsofsmart.cineaste.service.FilmService
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._


class Application @Inject()(filmService: FilmService) extends Controller {

   def index = Action {
      Ok(views.html.home())
   }

   def films = Action.async {
      filmService.films.map {
         response =>
            Ok(views.html.films(response))
      }
   }

}
