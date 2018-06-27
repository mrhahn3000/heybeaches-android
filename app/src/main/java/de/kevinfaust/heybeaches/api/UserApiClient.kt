package de.kevinfaust.heybeaches.api

import de.kevinfaust.heybeaches.api.services.IWebService
import org.json.JSONObject

object UserApiClient : IApiClient {

    lateinit var webService: IWebService

    /**
     * Prepare Requests
     */

    override fun prepareGetRequest(path: String, token: String): ApiRequest {
        val method = "GET"
        val header = arrayListOf(Pair("Cache-Control", "no-cache"))

        if (token.isNotEmpty()) {
            header.add(Pair("x-auth", token))
        }

        val url = webService.createRequestUrl(path)

        return ApiRequest(method, url, header, null)
    }

    override fun preparePostRequest(path: String, body: JSONObject): ApiRequest {
        val method = "POST"
        val header = arrayListOf(Pair("Content-Type", "application/json"), Pair("Cache-Control", "no-cache"))

        val url = webService.createRequestUrl(path)

        return ApiRequest(method, url, header, body)
    }

    override fun prepareDeleteRequest(path: String, token: String): ApiRequest {
        val method = "DELETE"
        val header = arrayListOf(Pair("x-auth", token), Pair("Cache-Control", "no-cache"))

        val url = webService.createRequestUrl(path)

        return ApiRequest(method, url, header, null)
    }
}
