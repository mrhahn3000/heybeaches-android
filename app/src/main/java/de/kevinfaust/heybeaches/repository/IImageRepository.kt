package de.kevinfaust.heybeaches.repository

import android.graphics.Bitmap
import de.kevinfaust.heybeaches.model.Image

interface IImageRepository {
    fun getBeaches(page: Int = 0): ArrayList<Image>
    fun getBeachImage(image: Image): Bitmap?
}
