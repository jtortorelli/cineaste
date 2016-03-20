package com.wizardsofsmart.cineaste.respository

import java.net.ConnectException
import javax.inject.Inject

import com.wizardsofsmart.cineaste.domain.neo4j.Neo4jStatement
import com.wizardsofsmart.cineaste.respository.neo4j.Neo4jQueries
import com.wizardsofsmart.cineaste.value.error.Neo4jConnectionError
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global


class FilmRepository @Inject()(queries: Neo4jQueries) {

   def films = {
      queries.statementRequest.post(
         Json.toJson(
            Neo4jStatement.createStatement(
               queries.showcaseFilmsQuery))
               .toString()).map {
         response => Right(response)
      } recover {
         case _: ConnectException => Left(new Neo4jConnectionError)
      }
   }

   def film(uuid: String) = {
      queries.statementRequest.post(
         Json.toJson(
            Neo4jStatement.createStatement(
               queries.filmQuery(uuid)))
               .toString()).map {
         response => Right(response)
      } recover {
         case _: ConnectException => Left(new Neo4jConnectionError)
      }
   }

}
