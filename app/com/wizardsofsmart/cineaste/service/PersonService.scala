package com.wizardsofsmart.cineaste.service

import javax.inject.Inject

import com.wizardsofsmart.cineaste.domain.{Group, People, Person}
import com.wizardsofsmart.cineaste.respository.PersonRepository
import com.wizardsofsmart.cineaste.value.error.{DomainError, EmptyResultsError}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class PersonService @Inject()(personRepository: PersonRepository) {
   def people: Future[Either[DomainError, Seq[People]]] = {
      personRepository.people.map {
         case Right(response) =>
            val json = Json.parse(response.body) \\ "data"
            val persons = for (row <- json(0) \\ "row") yield row(0).as[Person]
            val groups = for (row <- json(1) \\ "row") yield row(0).as[Group]
            val people = persons ++ groups
            Right(people.sortBy(_.sortName))
         case Left(error) => Left(error)
      }
   }

   def person(uuid: String): Future[Either[DomainError, Person]] = {
      personRepository.person(uuid).map {
         case Right(response) =>
            val persons = for (row <- Json.parse(response.body) \\ "row") yield row(0).as[Person]
            if (persons.isEmpty) {
               Left(new EmptyResultsError)
            } else {
               Right(persons(0))
            }
         case Left(error) => Left(error)
      }
   }

}
