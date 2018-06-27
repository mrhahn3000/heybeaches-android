package de.kevinfaust.heybeaches.api.services

import de.kevinfaust.heybeaches.api.ApiRequest
import de.kevinfaust.heybeaches.api.ApiResult
import java.net.URL

interface IWebService {
    fun getRequest(request: ApiRequest): ApiResult
    fun postRequest(request: ApiRequest): ApiResult
    fun deleteRequest(request: ApiRequest)

    fun createRequestUrl(path: String): URL
}
