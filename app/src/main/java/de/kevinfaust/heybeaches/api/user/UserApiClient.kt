package de.kevinfaust.heybeaches.api.user

import de.kevinfaust.heybeaches.api.ApiRequest
import de.kevinfaust.heybeaches.api.ApiResult
import de.kevinfaust.heybeaches.api.IApiClient
import de.kevinfaust.heybeaches.api.services.IWebService
import org.json.JSONObject
import java.util.concurrent.ExecutorService

object UserApiClient : IApiClient {

    private const val REGISTRATION_ENDPOINT = "user/register"
    private const val LOGIN_ENDPOINT = "user/login"
    private const val LOGOUT_ENDPOINT = "user/logout"

    lateinit var webService: IWebService
    lateinit var executorService: ExecutorService


    fun signup(email: String, password: String): UserApiResult? {
        val body = createAuthBody(email, password)

        val request = preparePostRequest(REGISTRATION_ENDPOINT, body)

        var response: ApiResult? = null
        var result: UserApiResult? = null

        executorService.submit {
            response = webService.postRequest(request)
        }.get()


        if (response != null) {
            result = parseAuthResponse(response as ApiResult)
        }

        return result
    }

    fun login(email: String, password: String): UserApiResult? {
        val body = createAuthBody(email, password)

        val request = preparePostRequest(LOGIN_ENDPOINT, body)

        var response: ApiResult? = null
        var result: UserApiResult? = null

        executorService.submit {
            response = webService.postRequest(request)
        }.get()


        if (response != null) {
            result = parseAuthResponse(response as ApiResult)
        }

        return result
    }

    fun logout(token: String) {

        val request = prepareDeleteRequest(LOGOUT_ENDPOINT, token)

        executorService.submit {
            webService.deleteRequest(request)
        }.get()
    }

    // Example request body:
    //
    // {
    //   "email": "user@example.com",
    //   "password": "test1234"
    // }
    private fun createAuthBody(email: String, password: String): JSONObject {
        return JSONObject("{\"email\":\"$email\",\"password\":\"$password\"}")
    }

    // Example response body:
    //
    // {
    //   "_id": "58c15c8dec1fe2000ef290b1",
    //   "email": "user@example.com"
    // }
    private fun parseAuthResponse(response: ApiResult): UserApiResult {
        val jsonObject = JSONObject(response.response)
        val id = jsonObject.getString("_id")
        val email = jsonObject.getString("email")
        val token = response.token

        return UserApiResult(id, email, token)
    }

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
