package de.kevinfaust.heybeaches.api

import org.json.JSONObject

interface IApiClient {
    fun prepareGetRequest(path: String, token: String): ApiRequest
    fun preparePostRequest(path: String, body: JSONObject): ApiRequest
    fun prepareDeleteRequest(path: String, token: String): ApiRequest
}
