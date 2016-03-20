package controllers

import javax.inject.Inject

import com.wizardsofsmart.cineaste.service.PersonService
import play.api.mvc.{Action, Controller}

class PersonController @Inject()(personService: PersonService) extends Controller {

   def people = Action {
      Ok(personService.people)
   }

}
