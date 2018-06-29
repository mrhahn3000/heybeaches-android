package de.kevinfaust.heybeaches

import android.content.Context
import de.kevinfaust.heybeaches.api.image.ImageApiClient
import de.kevinfaust.heybeaches.api.services.IWebService
import de.kevinfaust.heybeaches.api.services.WebService
import de.kevinfaust.heybeaches.api.user.UserApiClient
import de.kevinfaust.heybeaches.repository.IImageRepository
import de.kevinfaust.heybeaches.repository.IUserRepository
import de.kevinfaust.heybeaches.repository.ImageRepository
import de.kevinfaust.heybeaches.repository.UserRepository
import de.kevinfaust.heybeaches.services.PersistenceService
import java.util.concurrent.Executors

object Injector {

    private fun userRepository(context: Context): IUserRepository {
        val userRepository = UserRepository
        userRepository.apiClient = Injector.userApiClient()
        userRepository.persistenceService = Injector.persistenceService(context)

        return userRepository
    }

    private fun imageRepository(context: Context): IImageRepository {
        val imageRepository = ImageRepository
        imageRepository.apiClient = Injector.imageApiClient()
        imageRepository.persistenceService = Injector.persistenceService(context)

        return imageRepository
    }

    private fun userApiClient(): UserApiClient {
        val userApiClient = UserApiClient
        userApiClient.webService = Injector.webService()
        userApiClient.executorService = Executors.newCachedThreadPool()

        return userApiClient
    }

    private fun imageApiClient(): ImageApiClient {
        val imageApiClient = ImageApiClient
        imageApiClient.webService = Injector.webService()
        imageApiClient.executorService = Executors.newCachedThreadPool()

        return imageApiClient
    }

    private fun persistenceService(context: Context): PersistenceService {
        return PersistenceService(context)
    }

    private fun webService(): IWebService {
        return WebService
    }

    fun inject(mainActivity: MainActivity) {
        mainActivity.userRepository = Injector.userRepository(mainActivity.applicationContext)
        mainActivity.imageRepository = Injector.imageRepository(mainActivity.applicationContext)
        mainActivity.persistenceService = Injector.persistenceService(mainActivity.applicationContext)
    }
}
