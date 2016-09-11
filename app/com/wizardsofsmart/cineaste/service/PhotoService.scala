package com.wizardsofsmart.cineaste.service

import java.io.File
import javax.imageio.ImageIO
import javax.inject.Inject

import com.wizardsofsmart.cineaste.value.Photo
import play.api.Play
import play.api.Play.current
import play.api.cache.CacheApi

class PhotoService @Inject()(cache: CacheApi) {

  def retrieveGallery(dir: String): Option[List[Photo]] = {
    val source = Play.getExistingFile(dir)
    source.map { existingDir =>
      val names = existingDir.list().toList.filter(_.endsWith(".jpg")).sorted
      val photos = names.map { name =>
        val path = dir + "/" + name
        val maybeHeight = cache.get[Int](path + ".height")
        val maybeWidth = cache.get[Int](path + ".width")
        maybeHeight.map { mh =>
          maybeWidth.map { mw =>
            Photo(name, mh, mw)
          } getOrElse {
            buildPhoto(path, name)
          }
        } getOrElse {
          buildPhoto(path, name)
        }
      }
      Some(photos)
    } getOrElse None
  }

  private def buildPhoto(path: String, name: String): Photo = {
    val bimg = ImageIO.read(new File(path))
    val width = bimg.getWidth
    val height = bimg.getHeight
    cache.set(path + ".height", height)
    cache.set(path + ".width", width)
    Photo(name, height, width)
  }

}
