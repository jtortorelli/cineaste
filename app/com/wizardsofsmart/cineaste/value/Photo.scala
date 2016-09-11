package com.wizardsofsmart.cineaste.value

case class Photo(fileName: String, height: Int, width: Int) {
  val thumbHeight = (150 * height) / width
}
