package com.wizardsofsmart.cineaste.value.error

case class Neo4jConnectionError() extends DomainError {
   override val message: String = "Could not connect to Neo4j"
}

