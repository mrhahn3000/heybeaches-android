package de.kevinfaust.heybeaches.api.image

import de.kevinfaust.heybeaches.api.ApiRequest
import de.kevinfaust.heybeaches.api.ApiResult
import de.kevinfaust.heybeaches.api.IApiClient
import de.kevinfaust.heybeaches.api.services.IWebService
import de.kevinfaust.heybeaches.model.Image
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.ExecutorService

object ImageApiClient : IApiClient {

    private const val BEACHES_ENDPOINT = "beaches"

    lateinit var webService: IWebService
    lateinit var executorService: ExecutorService

    fun getBeaches(page: Int, token: String): ImageApiResult? {
        val request = prepareGetRequest("$BEACHES_ENDPOINT?page=$page", token)

        var response: ApiResult? = null
        var result: ImageApiResult? = null

        executorService.submit {
            response = webService.getRequest(request)
        }.get()


        if (response != null) {
            result = parseImageResponse(response as ApiResult)
        }

        return result
    }

    // Example response body:
    //
    // [
    //   {
    //     "_id":"58b5784dde02e92969c8bf3b",
    //     "name":"63f08c32-9ba2-49af-a385-39fc0f428d1d.png",
    //     "url":"images/63f08c32-9ba2-49af-a385-39fc0f428d1d.png",
    //     "width":"410",
    //     "height":"560"
    //   },
    //   ...
    // ]
    private fun parseImageResponse(response: ApiResult): ImageApiResult {
        val jsonArray = JSONArray(response.response)
        val responseData = ArrayList<Image>()

        for (item in 0 until jsonArray.length()) {
            // Get the object at the current position
            val jsonObject = jsonArray.getJSONObject(item)
            // Parse each element of interest
            val name = jsonObject.getString("name")
            val path = jsonObject.getString("url")
            val width = jsonObject.getInt("width")
            val height = jsonObject.getInt("height")
            // Add an image object to the list
            responseData.add(Image(name, path, width, height))
        }

        return ImageApiResult(responseData)
    }

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun prepareDeleteRequest(path: String, token: String): ApiRequest {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
