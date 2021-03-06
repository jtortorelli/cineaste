package com.wizardsofsmart.cineaste.respository.neo4j

import javax.inject.Inject

import play.api.libs.ws.WSClient

class Neo4jQueries @Inject()(ws: WSClient) {

   val GROUP = "Group"
   val PERSON = "Person"

   def statementRequest = {
      ws.url(statementURL).withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
   }

   def statementURL = {
      "http://localhost:7474/db/data/transaction/commit"
   }

   // Film queries
   def showcasedFilmsQuery = {
      "match (n:Film {showcase: true}) return n"
   }

   def showcasedFilmQuery(uuid: String) = {
      s"""match (n:Film {uuid: \"$uuid\", showcase: true}) return n"""
   }

   def filmStaffQuery(uuid: String) = {
      s"""match (n:Person)-[r:WORKED_ON]->(m:Film {uuid: \"$uuid\"}) where r.role <> \"Actor\" with n, r, m order by r.order match (n)-[r]->(m) with r.role as role, collect(n) as people, collect(r.order) as orders order by head(orders) return {role: role, people: people, orders: orders}"""
   }

   def filmCastQuery(uuid: String) = {
      s"""match (n)-[r:WORKED_ON {role: \"Actor\"}]->(m:Film {uuid: \"$uuid\"}) with n, r, m order by r.order, n.last_name, n.name match (n)-[r]->(m) with n as people, collect(r.character) as characters, collect(r.order) as orders order by head(orders), n.last_name, n.name return {people: people, characters: characters, orders: orders}"""
   }

   def filmStudioQuery(uuid: String) = {
      s"""match (n:Studio)-[r:PRODUCED]->(m:Film {uuid: \"$uuid\"}) return n order by r.order"""
   }

   def filmSeriesQuery(uuid: String) = {
      s"""match (f:Film {uuid: \"$uuid\"})-[r:ENTRY]->(s:Series) with f, r, s optional match (p:Film {showcase: true})-[:ENTRY {entry_no: r.entry_no - 1}]->(s) optional match (a:Film {showcase: true})-[:ENTRY {entry_no: r.entry_no + 1}]->(s) return {precedent: p, antecedent: a}"""
   }

   // Person queries
   def showcasedPeopleQuery = {
      "match (n:Person {showcase: true}) return n"
   }

   def showcasedPersonQuery(uuid: String) = {
      s"""match (n:Person {uuid: \"$uuid\", showcase: true}) return n"""
   }

   def showcasedGroupsQuery = {
      s"""match (n:Group {showcase: true}) return n"""
   }

   def showcasedGroupQuery(uuid: String) = {
      s"""match (n:Group {uuid: \"$uuid\", showcase: true}) return n"""
   }

   def groupMembersQuery(uuid: String) = {
      s"""match (n:Person)-[:MEMBER_OF]->(g:Group {uuid: \"$uuid\"}) return n"""
   }

   def groupStaffCreditsQuery(uuid: String) = {
      staffCreditsQuery(uuid, GROUP)
   }

   def groupCastCreditsQuery(uuid: String) = {
      castCreditsQuery(uuid, GROUP)
   }

   def personStaffCreditsQuery(uuid: String) = {
      staffCreditsQuery(uuid, PERSON)
   }

   def personCastCreditsQuery(uuid: String) = {
      castCreditsQuery(uuid, PERSON)
   }

   def castCreditsQuery(uuid: String, label: String) = {
      s"""match (n:$label {uuid: \"$uuid\"})-[r:WORKED_ON {role: \"Actor\"}]->(m:Film) with distinct m as film, collect(r.character) as characters return {film: film, characters: characters}"""
   }

   def staffCreditsQuery(uuid: String, label: String) = {
      s"""match (n:$label {uuid: \"$uuid\"})-[r:WORKED_ON]->(m:Film) where r.role <> \"Actor\" with distinct r.role as role, collect(m) as films return {role: role, films: films}"""
   }

}
