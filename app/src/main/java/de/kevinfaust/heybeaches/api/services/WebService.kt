package de.kevinfaust.heybeaches.api.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import de.kevinfaust.heybeaches.api.ApiRequest
import de.kevinfaust.heybeaches.api.ApiResult
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.HttpURLConnection.*
import java.net.URL

object WebService : IWebService {
    private const val BASE_URL = "http://techtest.lab1886.io:3000/"

    override fun getRequest(request: ApiRequest): ApiResult {
        var result: ApiResult? = null

        try {
            val connection = performRequest(request)

            when (connection.responseCode) {
                HTTP_OK -> {
                    result = if (connection.contentType == "image/png") {
                        val bitmap = BitmapFactory.decodeStream(connection.inputStream)
                        var responseSuccess = ""

                        if (bitmap != null) {
                            responseSuccess = handleImageResponse(bitmap)
                        }

                        ApiResult(responseSuccess, "")
                    } else {
                        val responseSuccess = connection.inputStream.bufferedReader().readText()
                        ApiResult(responseSuccess, "")
                    }
                }
                HTTP_BAD_REQUEST -> {
                    val responseError = connection.errorStream.bufferedReader().readText()
                    // TODO: Handle Error correctly
                    Log.e("WebService", "getRequest: ${connection.responseCode} with response $responseError")
                }
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

            when (connection.responseCode) {
                HTTP_OK -> {
                    val responseSuccess = connection.inputStream.bufferedReader().readText()
                    val token = connection.getHeaderField("x-auth")
                    result = ApiResult(responseSuccess, token)
                }
                HTTP_BAD_REQUEST -> {
                    val responseError = connection.errorStream.bufferedReader().readText()
                    // TODO: Handle Error correctly
                    Log.e("WebService", "postRequest: ${connection.responseCode} with response $responseError")
                }
            }
        } catch (e: Exception) {
            Log.e("WebService", "postRequest: An error occurred due to $e")
        }

        return result!!
    }

    override fun deleteRequest(request: ApiRequest) {

        try {
            val connection = performRequest(request)

            when (connection.responseCode) {
                HTTP_OK -> {
                    Log.i("WebService", "deleteRequest: User was logged out")
                }
                HTTP_UNAUTHORIZED -> {
                    // TODO: Handle Error correctly
                    Log.e("WebService", "deleteRequest: ${connection.responseCode} Unauthorized. Wrong token.")
                }
                HTTP_BAD_REQUEST -> {
                    val responseError = connection.errorStream.bufferedReader().readText()
                    // TODO: Handle Error correctly
                    Log.e("WebService", "deleteRequest: ${connection.responseCode} with response $responseError")
                }
            }
        } catch (e: Exception) {
            Log.e("WebService", "deleteRequest: An error occurred due to $e")
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

    private fun handleImageResponse(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }
}
