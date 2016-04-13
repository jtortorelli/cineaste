package com.wizardsofsmart.cineaste.respository

import java.net.ConnectException
import javax.inject.Inject

import com.wizardsofsmart.cineaste.domain.neo4j.Neo4jStatement
import com.wizardsofsmart.cineaste.respository.neo4j.Neo4jQueries
import com.wizardsofsmart.cineaste.value.error.{DomainError, Neo4jConnectionError}
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class PersonRepository @Inject()(queries: Neo4jQueries) {

   def people: Future[Either[DomainError, WSResponse]] = {
      queries.statementRequest.post(
         Json.toJson(
            Neo4jStatement.createStatements(
               queries.showcasedPeopleQuery,
               queries.showcasedGroupsQuery
            )
         ).toString()).map {
         response => Right(response)
      } recover {
         case _: ConnectException => Left(new Neo4jConnectionError)
      }
   }

   def person(uuid: String): Future[Either[DomainError, WSResponse]] = {
      queries.statementRequest.post(
         Json.toJson(
            Neo4jStatement.createStatements(
               queries.showcasedPersonQuery(uuid),
               queries.personStaffCreditsQuery(uuid),
               queries.personCastCreditsQuery(uuid)
            )
         ).toString()).map {
         response => Right(response)
      } recover {
         case _: ConnectException => Left(new Neo4jConnectionError)
      }
   }

   def group(uuid: String): Future[Either[DomainError, WSResponse]] = {
      queries.statementRequest.post(
         Json.toJson(
            Neo4jStatement.createStatements(
               queries.showcasedGroupQuery(uuid)
            )
         ).toString()).map {
         response => Right(response)
      } recover {
         case _: ConnectException => Left(new Neo4jConnectionError)
      }
   }


}
