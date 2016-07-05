package controllers

import javax.inject.Inject

import com.wizardsofsmart.cineaste.service.PeopleService
import com.wizardsofsmart.cineaste.value.error.{EmptyResultsError, Neo4jConnectionError}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global


class PeopleController @Inject()(peopleService: PeopleService) extends Controller {

   def people = Action.async {
      peopleService.people.map {
         case Right(response) => Ok(views.html.people.people(response))
         case Left(error) => error match {
            case e: Neo4jConnectionError => ServiceUnavailable(views.html.errors.serviceUnavailable(e.message))
            case _ => InternalServerError(views.html.errors.internalServerError())
         }
      }
   }

   def person(uuid: String) = Action.async {
      peopleService.person(uuid).map {
         case Right(response) => Ok(views.html.people.person(response._1, response._2, response._3))
         case Left(error) => error match {
            case e: Neo4jConnectionError => ServiceUnavailable(views.html.errors.serviceUnavailable(e.message))
            case e: EmptyResultsError => NotFound(views.html.errors.notFound(e.message))
            case _ => InternalServerError(views.html.errors.internalServerError())
         }
      }
   }

   def group(uuid: String) = Action.async {
      peopleService.group(uuid).map {
         case Right(response) => Ok(views.html.people.group(response._1, response._2, response._3))
         case Left(error) => error match {
            case e: Neo4jConnectionError => ServiceUnavailable(views.html.errors.serviceUnavailable(e.message))
            case e: EmptyResultsError => NotFound(views.html.errors.notFound(e.message))
            case _ => InternalServerError(views.html.errors.internalServerError())
         }
      }
   }

}
