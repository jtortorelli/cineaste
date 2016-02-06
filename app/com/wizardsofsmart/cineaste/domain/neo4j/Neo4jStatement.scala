package com.wizardsofsmart.cineaste.domain.neo4j

import play.api.libs.json.{Json, Writes}

case class Statement(query: String)

object Statement {
  implicit val statementWrites = new Writes[Statement] {
    def writes(statement: Statement) = Json.obj(
      "statement" -> statement.query
    )
  }
}

case class Neo4jStatement(statements: Seq[Statement])

object Neo4jStatement {
  implicit val neo4jStatementWrites = new Writes[Neo4jStatement] {
    def writes(neo4jStatement: Neo4jStatement) = Json.obj(
      "statements" -> neo4jStatement.statements
    )
  }
}

