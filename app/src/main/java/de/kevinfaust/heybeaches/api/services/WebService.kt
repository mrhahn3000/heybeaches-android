package de.kevinfaust.heybeaches.api.services

import android.util.Log
import de.kevinfaust.heybeaches.api.ApiRequest
import de.kevinfaust.heybeaches.api.ApiResult
import java.io.BufferedOutputStream
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL

object WebService : IWebService {
    private const val BASE_URL = "http://techtest.lab1886.io:3000/"

    override fun getRequest(request: ApiRequest): ApiResult {
        var result: ApiResult? = null

        try {
            val connection = performRequest(request)

            if (connection.responseCode == HTTP_OK) {
                val responseBody = connection.inputStream.bufferedReader().readText()

                result = ApiResult(responseBody, "")
            }
        } catch (e: Exception) {
            Log.e("WebService", "getRequest: An error occurred due to $e")
        }

        return result!!
    }

    override fun postRequest(request: ApiRequest): ApiResult {
        var result: ApiResult? = null

        try {
            val connection = performRequest(request)

            if (connection.responseCode == HTTP_OK) {
                val responseBody = connection.inputStream.bufferedReader().readText()
                val token = connection.getHeaderField("x-auth")

                result = ApiResult(responseBody, token)
            }
        } catch (e: Exception) {
            Log.e("WebService", "postRequest: An error occurred due to $e")
        }

        return result!!
    }

    override fun deleteRequest(request: ApiRequest) {

        try {
            val connection = performRequest(request)

            if (connection.responseCode == HTTP_OK) {
                Log.i("WebService", "deleteRequest: User was logged out")
            }
        } catch (e: Exception) {
            Log.e("WebService", "postRequest: An error occurred due to $e")
        }
    }

    override fun createRequestUrl(path: String): URL {
        return URL("$BASE_URL$path")
    }

    private fun performRequest(request: ApiRequest): HttpURLConnection {
        // Open the connection with the given url
        val connection = request.url.openConnection() as HttpURLConnection

        // Set the connection method
        connection.requestMethod = request.method

        // Add all respective request headers to the connection
        for (header in request.header) {
            connection.setRequestProperty(header.first, header.second)
        }

        // Do the post request
        if (connection.requestMethod == "POST") {
            try {
                connection.doOutput = true

                if (request.body != null) {
                    val out = BufferedOutputStream(connection.outputStream)
                    out.write(request.body.toString().toByteArray())
                    out.flush()
                    out.close()
                }
            } catch (e: Exception) {
                Log.e("WebService", "performRequest: An error occurred due to $e")
            }
        }

        return connection
    }
}
