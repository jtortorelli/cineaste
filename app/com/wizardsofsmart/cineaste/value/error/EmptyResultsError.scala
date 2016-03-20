package com.wizardsofsmart.cineaste.value.error

case class EmptyResultsError() extends DomainError {
   override val message: String = "Query returned zero results"
}
