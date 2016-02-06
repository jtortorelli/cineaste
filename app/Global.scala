package com.wizardofsmart.cineaste

import play.api.GlobalSettings

object Global extends GlobalSettings {
  val neo4jEndpoint: String = "http://localhost:7474/db/data/transaction/commit"

}