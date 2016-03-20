package controllers

import play.api.mvc.{Action, Controller}

class PersonController extends Controller {

   def people = Action {
      Ok("This is the people page")
   }

}
