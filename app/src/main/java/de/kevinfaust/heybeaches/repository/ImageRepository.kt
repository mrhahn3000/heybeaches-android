package de.kevinfaust.heybeaches.repository

import android.graphics.Bitmap
import android.util.Log
import de.kevinfaust.heybeaches.api.image.ImageApiClient
import de.kevinfaust.heybeaches.model.Image
import de.kevinfaust.heybeaches.services.PersistenceService

object ImageRepository : IImageRepository {
    lateinit var apiClient: ImageApiClient
    lateinit var persistenceService: PersistenceService

    override fun getBeaches(page: Int): ArrayList<Image> {
        var beachList = ArrayList<Image>()

        try {
            // 1. Get token for authorized access
            if (persistenceService.jwtToken.isNotEmpty()) {
                val token = persistenceService.jwtToken

                // 2. Make webservice call
                val result = apiClient.getBeaches(page, token)

                // 3. Create the image list
                if (result != null) {
                    beachList = result.response
                } else {
                    Log.e("IMAGE_REPO", "getBeaches: Empty response")
                }
            } else {
                Log.e("IMAGE_REPO", "getBeaches: Not Authorized to access beach images")
            }
        } catch (e: Exception) {
            Log.e("IMAGE_REPO", "getBeaches: Failed to fetch beach images due to $e")
        }

        return beachList
    }

    override fun getBeachImage(image: Image): Bitmap? {
        var bitmap: Bitmap? = null

        try {
            // 1. Get token for authorized access
            if (persistenceService.jwtToken.isNotEmpty()) {
                val token = persistenceService.jwtToken

                // 2. Make webservice call
                val result = apiClient.getBeachImage(image.path, token)

                // 3. Set the image
                bitmap = result
            } else {
                Log.e("IMAGE_REPO", "getBeachImage: Not Authorized to access beach images")
            }
        } catch (e: Exception) {
            Log.e("IMAGE_REPO", "getBeachImage: Failed to fetch specific image due to $e")
        }

        return bitmap
    }
}
