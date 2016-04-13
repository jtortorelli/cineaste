package com.wizardsofsmart.cineaste.service

import javax.inject.Inject

import com.wizardsofsmart.cineaste.domain.people.{Group, People, Person, PersonStaffRole}
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

   def person(uuid: String): Future[Either[DomainError, (Person, Seq[PersonStaffRole])]] = {
      personRepository.person(uuid).map {
         case Right(response) =>
            val json = Json.parse(response.body) \\ "data"
            val persons = for (row <- json(0) \\ "row") yield row(0).as[Person]
            val staffRoles = for (row <- json(1) \\ "row") yield row(0).as[PersonStaffRole]
            if (persons.isEmpty) {
               Left(new EmptyResultsError)
            } else {
               Right((persons(0), staffRoles))
            }
         case Left(error) => Left(error)
      }
   }

   def group(uuid: String): Future[Either[DomainError, Group]] = {
      personRepository.group(uuid).map {
         case Right(response) =>
            val groups = for (row <- Json.parse(response.body) \\ "row") yield row(0).as[Group]
            if (groups.isEmpty) {
               Left(new EmptyResultsError)
            } else {
               Right(groups(0))
            }
         case Left(error) => Left(error)
      }
   }

}
