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


class FilmRepository @Inject()(queries: Neo4jQueries) {

   def films: Future[Either[DomainError, WSResponse]] = {
      queries.statementRequest.post(
         Json.toJson(
            Neo4jStatement.createStatement(
               queries.showcasedFilmsQuery))
               .toString()).map {
         response => Right(response)
      } recover {
         case _: ConnectException => Left(new Neo4jConnectionError)
      }
   }

   def film(uuid: String): Future[Either[DomainError, WSResponse]] = {
      queries.statementRequest.post(
         Json.toJson(
            Neo4jStatement.createStatements(
               queries.showcasedFilmQuery(uuid),
               queries.filmStaffQuery(uuid),
               queries.filmCastQuery(uuid),
               queries.filmStudioQuery(uuid),
               queries.filmSeriesQuery(uuid)))
               .toString()).map {
         response => Right(response)
      } recover {
         case _: ConnectException => Left(new Neo4jConnectionError)
      }
   }

}
