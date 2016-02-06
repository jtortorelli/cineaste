package com.wizardsofsmart.cineaste.service

import javax.inject.Inject

import com.google.inject.ImplementedBy
import com.wizardofsmart.cineaste.Global
import com.wizardsofsmart.cineaste.domain.Film
import com.wizardsofsmart.cineaste.repository.FilmRepository
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.libs.ws.WSClient

import scala.concurrent.Future


@ImplementedBy(classOf[DefaultFilmService])
trait FilmService {
  def retrieveFilmsList: Future[JsObject]
}

class DefaultFilmService @Inject()(ws: WSClient)(filmRepository: FilmRepository) extends FilmService {
  val request = ws.url(Global.neo4jEndpoint).withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")

  override def retrieveFilmsList = {
    filmRepository.getAllFilms.map { response =>
      val rows: Seq[JsValue] = {
        Json.parse(response.body) \\ "row"
      }
      val films = for (row <- rows) yield row(0).as[Film]
      Json.obj("data" -> Json.toJson(films))
    }
  }
}
