package controllers

import javax.inject.Inject

import com.wizardsofsmart.cineaste.service.PersonService
import com.wizardsofsmart.cineaste.value.error.{EmptyResultsError, Neo4jConnectionError}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global


class PersonController @Inject()(personService: PersonService) extends Controller {

   def people = Action.async {
      personService.people.map {
         case Right(response) => Ok(views.html.people.people(response))
         case Left(error) => error match {
            case e: Neo4jConnectionError => ServiceUnavailable(e.message)
            case _ => InternalServerError
         }
      }
   }

   def person(uuid: String) = Action.async {
      personService.person(uuid).map {
         case Right(response) => Ok(views.html.people.person(response))
         case Left(error) => error match {
            case e: Neo4jConnectionError => ServiceUnavailable(e.message)
            case e: EmptyResultsError => NotFound(e.message)
            case _ => InternalServerError
         }
      }
   }

}
