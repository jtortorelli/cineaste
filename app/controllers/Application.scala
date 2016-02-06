package controllers

import javax.inject.Inject

import com.wizardofsmart.cineaste.Global
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc._

class Application @Inject() (ws: WSClient) extends Controller {

  def index = Action.async {
    val url = Global.neo4jEndpoint
    val request = ws.url(url)
    val complexRequest = request.withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
    val jsonString = Json.parse(
      """
        {
        "statements": [{
        "statement": "match (n:Film) return n"
        }]
        }
      """)
    val result = complexRequest.post(jsonString.toString())
    result.map { response =>
        Ok(response.body)
    }
  }

}
