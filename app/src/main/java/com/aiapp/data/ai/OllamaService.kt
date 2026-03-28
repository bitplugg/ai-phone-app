package com.aiapp.data.ai

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

data class OllamaModel(
    val name: String,
    val size: Long,
    val modifiedAt: String
)

data class GenerateRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean = false,
    val options: GenerateOptions? = null
)

data class GenerateOptions(
    val temperature: Double = 0.7,
    val num_predict: Int = 512
)

data class GenerateResponse(
    val model: String,
    val response: String,
    val done: Boolean
)

class OllamaService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(180, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    private var serverUrl = "http://192.168.1.100:11434"

    companion object {
        val MODELS = listOf(
            ModelInfo("llama3.2", "Llama 3.2", "Meta - 3B", false),
            ModelInfo("llama3.1", "Llama 3.1", "Meta - 8B", false),
            ModelInfo("llama3", "Llama 3", "Meta Llama 3", false),
            ModelInfo("mistral", "Mistral", "Mistral AI", false),
            ModelInfo("phi3", "Phi-3", "Microsoft Phi-3", false),
            ModelInfo("gemma2", "Gemma 2", "Google Gemma 2", false),
            ModelInfo("qwen2.5", "Qwen 2.5", "Alibaba Qwen", false),
            ModelInfo("tinyllama", "TinyLlama", "Компактная 1B", false)
        )
    }

    fun setServerUrl(url: String) {
        serverUrl = url
    }

    fun getServerUrl(): String = serverUrl

    fun isConfigured(): Boolean = true

    suspend fun checkConnection(): Boolean = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$serverUrl/api/tags")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                response.isSuccessful
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getModels(): List<ModelInfo> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$serverUrl/api/tags")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string() ?: return@withContext emptyList()
                    val json = gson.fromJson(body, JsonObject::class.java)
                    val models = json.getAsJsonArray("models") ?: JsonArray()
                    
                    models.mapNotNull { modelJson ->
                        try {
                            val model = modelJson.asJsonObject
                            ModelInfo(
                                id = model.get("name").asString.replace(":latest", ""),
                                name = model.get("name").asString,
                                description = "Модель Ollama",
                                isDownloaded = true
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                } else {
                    MODELS
                }
            }
        } catch (e: Exception) {
            MODELS
        }
    }

    fun generate(model: String, prompt: String): Flow<String> = flow {
        try {
            val requestBody = GenerateRequest(
                model = model,
                prompt = prompt,
                stream = false
            )

            val json = gson.toJson(requestBody)
            val body = json.toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("$serverUrl/api/generate")
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: ""
                    val jsonResponse = gson.fromJson(responseBody, GenerateResponse::class.java)
                    emit(jsonResponse.response)
                } else {
                    emit("Ошибка: ${response.code}\n\nПроверьте:\n1. Ollama запущен на ПК\n2. Правильный IP адрес в настройках")
                }
            }
        } catch (e: Exception) {
            emit("Ошибка соединения: ${e.message}\n\nУбедитесь что:\n1. Ollama запущен\n2. Телефон в той же сети\n3. IP: 192.168.1.100 или ваш IP ПК")
        }
    }.flowOn(Dispatchers.IO)

    fun generateStream(model: String, prompt: String): Flow<String> = flow {
        try {
            val requestBody = GenerateRequest(
                model = model,
                prompt = prompt,
                stream = true
            )

            val json = gson.toJson(requestBody)
            val body = json.toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("$serverUrl/api/generate")
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: ""
                    val lines = responseBody.split("\n").filter { it.isNotBlank() }
                    
                    for (line in lines) {
                        try {
                            val jsonResponse = gson.fromJson(line, GenerateResponse::class.java)
                            emit(jsonResponse.response)
                            if (jsonResponse.done) break
                        } catch (e: Exception) {
                            // Skip
                        }
                    }
                } else {
                    emit("Ошибка: ${response.code}")
                }
            }
        } catch (e: Exception) {
            emit("Ошибка: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)
}

data class ModelInfo(
    val id: String,
    val name: String,
    val description: String,
    val isDownloaded: Boolean = false
)
