package controllers

import javax.inject.Inject

import com.wizardsofsmart.cineaste.service.PersonService
import com.wizardsofsmart.cineaste.value.error.Neo4jConnectionError
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global


class PersonController @Inject()(personService: PersonService) extends Controller {

   def people = Action.async {
      personService.people.map {
         case Right(response) => Ok(views.html.people(response))
         case Left(error) => error match {
            case _: Neo4jConnectionError => ServiceUnavailable("Could not connect to database")
            case _ => InternalServerError
         }
      }
   }

}
