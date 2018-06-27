package de.kevinfaust.heybeaches

import de.kevinfaust.heybeaches.api.IApiClient
import de.kevinfaust.heybeaches.api.user.UserApiClient
import de.kevinfaust.heybeaches.api.services.IWebService
import de.kevinfaust.heybeaches.api.services.WebService
import java.util.concurrent.Executors

object Injector {

    private fun userApiClient(): IApiClient {
        val userApiClient = UserApiClient
        userApiClient.webService = Injector.webService()
        userApiClient.executorService = Executors.newCachedThreadPool()

        return UserApiClient
    }

    private fun webService(): IWebService {
        return WebService
    }
}
