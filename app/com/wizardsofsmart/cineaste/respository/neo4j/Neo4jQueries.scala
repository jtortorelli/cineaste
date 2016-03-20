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

   // Film queries
   def showcasedFilmsQuery = {
      "match (n:Film) where n.showcase = true return n"
   }

   def showcasedFilmQuery(uuid: String) = {
      s"""match (n:Film {uuid: "$uuid"}) where n.showcase = true return n"""
   }

   // Person queries
   def peopleQuery = {
      "match (n:Person) return n"
   }

}
