package com.wizardsofsmart.cineaste.respository.neo4j

import javax.inject.Inject

import play.api.libs.ws.WSClient

class Neo4jQueries @Inject()(ws: WSClient) {

   def statementRequest = {
      ws.url(statementURL).withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
   }

   def statementURL = {
      "http://localhost:7474/db/data/transaction/commit"
   }

   def showcaseFilmsQuery = {
      "match (n:Film) where n.showcase = true return n"
   }

}
