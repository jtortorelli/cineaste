package com.wizardsofsmart.cineaste.service

import javax.inject.Inject

import com.wizardsofsmart.cineaste.domain.people.{Group, GroupMembers, People, Person, PersonCastCredit, PersonCastRole, PersonStaffRole, Role}
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

   def person(uuid: String): Future[Either[DomainError, (Person, Seq[Role])]] = {
      personRepository.person(uuid).map {
         case Right(response) =>
            val json = Json.parse(response.body) \\ "data"
            val persons = for (row <- json(0) \\ "row") yield row(0).as[Person]
            val staffRoles = for (row <- json(1) \\ "row") yield row(0).as[PersonStaffRole]
            val castRoles = for (row <- json(2) \\ "row") yield row(0).as[PersonCastCredit]
            if (persons.isEmpty) {
               Left(new EmptyResultsError)
            } else {
               val roles: Seq[Role] = if (castRoles.nonEmpty) staffRoles ++ Seq(PersonCastRole(castRoles)) else staffRoles
               Right((persons(0), roles))
            }
         case Left(error) => Left(error)
      }
   }

   def group(uuid: String): Future[Either[DomainError, (Group, Option[GroupMembers])]] = {
      personRepository.group(uuid).map {
         case Right(response) =>
            val json = Json.parse(response.body) \\ "data"
            val groups = for (row <- json(0) \\ "row") yield row(0).as[Group]
            val members = for (row <- json(1) \\ "row") yield row(0).as[GroupMembers]
            if (groups.isEmpty) {
               Left(new EmptyResultsError)
            } else {
               val membersOption = if (members(0).members.nonEmpty) Some(members(0)) else None
               Right((groups(0), membersOption))
            }
         case Left(error) => Left(error)
      }
   }

}
