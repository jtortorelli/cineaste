package com.wizardsofsmart.cineaste.respository

import java.net.ConnectException
import javax.inject.Inject

import com.wizardsofsmart.cineaste.domain.neo4j.Neo4jStatement
import com.wizardsofsmart.cineaste.respository.neo4j.Neo4jQueries
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global


class PersonRepository @Inject()(queries: Neo4jQueries) {

   def people = {
      queries.statementRequest.post(
         Json.toJson(
            Neo4jStatement.createStatement(
               queries.peopleQuery
            )
         ).toString()).map {
         response => Right(response)
      } recover {
         case _: ConnectException => Left("The database is offline.")
      }
   }


}
