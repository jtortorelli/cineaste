package com.wizardsofsmart.cineaste.repository

import javax.inject.Inject

import com.google.inject.ImplementedBy
import com.wizardofsmart.cineaste.Global
import com.wizardsofsmart.cineaste.domain.neo4j.{Statement, Neo4jStatement}
import play.api.libs.json.Json
import play.api.libs.ws.{WSResponse, WSClient}

import scala.concurrent.Future

@ImplementedBy(classOf[DefaultFilmRepository])
trait FilmRepository {
  def getAllFilms: Future[WSResponse]
}

class DefaultFilmRepository @Inject()(ws: WSClient) extends FilmRepository {
  override def getAllFilms = {
    val request = ws.url(Global.neo4jEndpoint).withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
    val neo4jStatement = createQuery("match (n:Film) return n")
    request.post(Json.toJson(neo4jStatement).toString())
  }

  def createQuery(query: String) = {
    new Neo4jStatement(List(new Statement(query)))
  }
}
