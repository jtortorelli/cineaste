package com.wizardsofsmart.cineaste.service

import javax.inject.Inject

import com.wizardsofsmart.cineaste.domain.Person
import com.wizardsofsmart.cineaste.respository.PersonRepository
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global


class PersonService @Inject()(personRepository: PersonRepository) {
   def people = {
      personRepository.people.map {
         case Right(response) =>
            val people = for (row <- Json.parse(response.body) \\ "row") yield row(0).as[Person]
            Right(people.sortBy(p => (p.lastName, p.firstName)))
         case Left(error) => Left(error)
      }
   }

}
