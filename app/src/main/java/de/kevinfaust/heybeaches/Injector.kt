package de.kevinfaust.heybeaches

import de.kevinfaust.heybeaches.api.IApiClient
import de.kevinfaust.heybeaches.api.UserApiClient
import de.kevinfaust.heybeaches.api.services.IWebService
import de.kevinfaust.heybeaches.api.services.WebService

object Injector {

    private fun userApiClient(): IApiClient {
        val userApiClient = UserApiClient
        userApiClient.webService = Injector.webService()

        return UserApiClient
    }

    private fun webService(): IWebService {
        return WebService
    }
}
