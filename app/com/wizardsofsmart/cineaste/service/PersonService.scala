package com.wizardsofsmart.cineaste.service

import javax.inject.Inject

import com.wizardsofsmart.cineaste.respository.PersonRepository

class PersonService @Inject()(personRepository: PersonRepository) {
   def people = {
      personRepository.people
   }

}
