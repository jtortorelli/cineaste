package com.wizardsofsmart.cineaste.service

import javax.inject.Inject

import com.wizardsofsmart.cineaste.respository.PersonRepository

import scala.concurrent.ExecutionContext.Implicits.global


class PersonService @Inject()(personRepository: PersonRepository) {
   def people = {
      personRepository.people.map {
         case Right(response) => Right(response)
         case Left(error) => Left(error)
      }
   }

}
