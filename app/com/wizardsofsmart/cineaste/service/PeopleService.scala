package com.wizardsofsmart.cineaste.service

import javax.inject.Inject

import com.wizardsofsmart.cineaste.domain.people.{Group, GroupMembers, People, Person, CastCredit, CastRole, StaffRole, Role}
import com.wizardsofsmart.cineaste.respository.PeopleRepository
import com.wizardsofsmart.cineaste.value.error.{DomainError, EmptyResultsError}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class PeopleService @Inject()(peopleRepository: PeopleRepository) {
   def people: Future[Either[DomainError, Seq[People]]] = {
      peopleRepository.people.map {
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
      peopleRepository.person(uuid).map {
         case Right(response) =>
            val json = Json.parse(response.body) \\ "data"
            val persons = for (row <- json(0) \\ "row") yield row(0).as[Person]
            val staffRoles = for (row <- json(1) \\ "row") yield row(0).as[StaffRole]
            val castRoles = for (row <- json(2) \\ "row") yield row(0).as[CastCredit]
            if (persons.isEmpty) {
               Left(new EmptyResultsError)
            } else {
               val roles: Seq[Role] = if (castRoles.nonEmpty) staffRoles ++ Seq(CastRole(castRoles)) else staffRoles
               Right((persons(0), roles))
            }
         case Left(error) => Left(error)
      }
   }

   def group(uuid: String): Future[Either[DomainError, (Group, Seq[Person], Seq[Role])]] = {
      peopleRepository.group(uuid).map {
         case Right(response) =>
            val json = Json.parse(response.body) \\ "data"
            val groups = for (row <- json(0) \\ "row") yield row(0).as[Group]
            val members = for (row <- json(1) \\ "row") yield row(0).as[Person]
            val staffRoles = for (row <- json(2) \\ "row") yield row(0).as[StaffRole]
            val castRoles = for (row <- json(3) \\ "row") yield row(0).as[CastCredit]
            if (groups.isEmpty) {
               Left(new EmptyResultsError)
            } else {
               val roles: Seq[Role] = if (castRoles.nonEmpty) staffRoles ++ Seq(CastRole(castRoles)) else staffRoles
               Right((groups(0), members, roles))
            }
         case Left(error) => Left(error)
      }
   }

}