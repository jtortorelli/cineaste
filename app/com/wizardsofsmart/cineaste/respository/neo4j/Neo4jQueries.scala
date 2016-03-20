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

   def filmStaffQuery(uuid: String) = {
      s"""match (n:Person)-[r:WORKED_ON]->(m:Film {uuid: \"$uuid\"}) where r.role <> \"Actor\" with n, r, m order by r.order match (n)-[r]->(m) with r.role as role, collect(n.first_name + \" \" + n.last_name) as names, collect(r.order) as orders order by head(orders) return {role: role, names: names, orders: orders}"""
   }

   // Person queries
   def peopleQuery = {
      "match (n:Person) return n"
   }

}
