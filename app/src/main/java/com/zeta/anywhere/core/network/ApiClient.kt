package com.zeta.anywhere.core.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object ApiClient {
    suspend fun postJson(url: String, body: String, accessToken: String? = null): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            accessToken?.let { token -> connection.setRequestProperty("Authorization", "Bearer " + token) }
            connection.doOutput = true

            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(body)
            }

            val stream = if (connection.responseCode in 200..299) connection.inputStream else connection.errorStream
            BufferedReader(stream.reader()).use { it.readText() }
        }
    }
}
