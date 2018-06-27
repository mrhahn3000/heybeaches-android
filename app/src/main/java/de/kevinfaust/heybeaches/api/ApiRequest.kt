package de.kevinfaust.heybeaches.api

import org.json.JSONObject
import java.net.URL

data class ApiRequest(val method: String, val url: URL, val header: ArrayList<Pair<String, String>>, val body: JSONObject?)
