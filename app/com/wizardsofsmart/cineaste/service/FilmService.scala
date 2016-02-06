package com.wizardsofsmart.cineaste.service

import javax.inject.Inject

import com.google.inject.ImplementedBy
import com.wizardofsmart.cineaste.Global
import com.wizardsofsmart.cineaste.domain.Film
import com.wizardsofsmart.cineaste.domain.neo4j.{Neo4jStatement, Statement}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSClient

import scala.concurrent.Future


@ImplementedBy(classOf[DefaultFilmService])
trait FilmService {
  def retrieveFilmsList: Future[Any]
}

class DefaultFilmService @Inject()(ws: WSClient) extends FilmService {
  val request = ws.url(Global.neo4jEndpoint).withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")

  override def retrieveFilmsList = {
    val neo4jStatement = createQuery("match (n:Film) return n")
    request.post(Json.toJson(neo4jStatement).toString()).map {
      response =>
        val rows: Seq[JsValue] = Json.parse(response.body) \\ "row"
        val films = for (row <- rows) yield row(0).as[Film]
        films
    }
  }

  def createQuery(query: String) = {
    new Neo4jStatement(List(new Statement(query)))
  }
}
