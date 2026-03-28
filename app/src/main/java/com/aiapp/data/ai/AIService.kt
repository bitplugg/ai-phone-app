package com.aiapp.data.ai

import com.google.gson.Gson
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

enum class AIMode {
    OLLAMA,
    CLOUD,
    DEEPSEEK
}

enum class CloudProvider {
    OPENAI,
    DEEPSEEK
}

data class AIResponse(
    val text: String,
    val promptTokens: Int = 0,
    val completionTokens: Int = 0
)

class AIService {
    private val ollamaService = OllamaService()
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(180, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()

    private var currentMode = AIMode.OLLAMA
    private var cloudProvider = CloudProvider.DEEPSEEK
    private var serverUrl = "http://192.168.1.100:11434"
    private var cloudApiKey = ""
    private var cloudModel = "deepseek-chat"

    fun setMode(mode: AIMode) {
        currentMode = mode
    }

    fun getMode(): AIMode = currentMode

    fun setCloudProvider(provider: CloudProvider) {
        cloudProvider = provider
        cloudModel = when (provider) {
            CloudProvider.OPENAI -> "gpt-4o-mini"
            CloudProvider.DEEPSEEK -> "deepseek-chat"
        }
    }

    fun getCloudProvider(): CloudProvider = cloudProvider

    fun setServerUrl(url: String) {
        serverUrl = url
        ollamaService.setServerUrl(url)
    }

    fun getServerUrl(): String = serverUrl

    fun setCloudApiKey(key: String) {
        cloudApiKey = key
    }

    fun getCloudApiKey(): String = cloudApiKey

    fun setCloudModel(model: String) {
        cloudModel = model
    }

    fun getCloudModel(): String = cloudModel

    fun isConfigured(): Boolean {
        return when (currentMode) {
            AIMode.OLLAMA -> true
            AIMode.CLOUD, AIMode.DEEPSEEK -> cloudApiKey.isNotBlank()
        }
    }

    suspend fun checkConnection(): Boolean {
        return when (currentMode) {
            AIMode.OLLAMA -> ollamaService.checkConnection()
            AIMode.CLOUD, AIMode.DEEPSEEK -> checkCloudConnection()
        }
    }

    private suspend fun checkCloudConnection(): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = when (cloudProvider) {
                CloudProvider.OPENAI -> "https://api.openai.com/v1/models"
                CloudProvider.DEEPSEEK -> "https://api.deepseek.com/v1/models"
            }
            val request = Request.Builder()
                .url(url)
                .header("Authorization", "Bearer $cloudApiKey")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                response.isSuccessful
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getModels(): List<ModelInfo> {
        return when (currentMode) {
            AIMode.OLLAMA -> ollamaService.getModels()
            AIMode.CLOUD -> getOpenAIModels()
            AIMode.DEEPSEEK -> getDeepSeekModels()
        }
    }

    private suspend fun getOpenAIModels(): List<ModelInfo> = withContext(Dispatchers.IO) {
        listOf(
            ModelInfo("gpt-4o", "GPT-4o", "OpenAI GPT-4o", true),
            ModelInfo("gpt-4o-mini", "GPT-4o Mini", "OpenAI GPT-4o Mini", true),
            ModelInfo("gpt-4-turbo", "GPT-4 Turbo", "OpenAI GPT-4 Turbo", true),
            ModelInfo("gpt-3.5-turbo", "GPT-3.5 Turbo", "OpenAI GPT-3.5 Turbo", true)
        )
    }

    private suspend fun getDeepSeekModels(): List<ModelInfo> = withContext(Dispatchers.IO) {
        listOf(
            ModelInfo("deepseek-chat", "DeepSeek Chat", "DeepSeek V3", true),
            ModelInfo("deepseek-coder", "DeepSeek Coder", "DeepSeek Coder V2", true)
        )
    }

    fun generate(prompt: String): Flow<AIResponse> = flow {
        when (currentMode) {
            AIMode.OLLAMA -> {
                val model = "llama3"
                val fullText = StringBuilder()
                ollamaService.generate(model, prompt).collect { 
                    fullText.append(it)
                    emit(AIResponse(it, 0, 0))
                }
                val promptTokens = estimateTokens(prompt)
                val completionTokens = estimateTokens(fullText.toString())
                emit(AIResponse(fullText.toString(), promptTokens, completionTokens))
            }
            AIMode.CLOUD -> {
                generateCloud(prompt, "https://api.openai.com/v1/chat/completions").collect { emit(it) }
            }
            AIMode.DEEPSEEK -> {
                generateCloud(prompt, "https://api.deepseek.com/chat/completions").collect { emit(it) }
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun estimateTokens(text: String): Int {
        return (text.length / 4) + 1
    }

    private fun generateCloud(prompt: String, apiUrl: String): Flow<AIResponse> = flow {
        try {
            val requestBody = mapOf(
                "model" to cloudModel,
                "messages" to listOf(mapOf("role" to "user", "content" to prompt)),
                "max_tokens" to 1000
            )

            val json = gson.toJson(requestBody)
            val body = json.toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url(apiUrl)
                .header("Authorization", "Bearer $cloudApiKey")
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: ""
                    val jsonResponse = gson.fromJson(responseBody, JsonObject::class.java)
                    
                    val usage = jsonResponse.getAsJsonObject("usage")
                    val promptTokens = usage?.get("prompt_tokens")?.asInt ?: estimateTokens(prompt)
                    val completionTokens = usage?.get("completion_tokens")?.asInt ?: 0
                    
                    val choices = jsonResponse.getAsJsonArray("choices")
                    if (choices != null && choices.size() > 0) {
                        val message = choices[0].asJsonObject.getAsJsonObject("message")
                        val content = message?.get("content")?.asString ?: ""
                        emit(AIResponse(content, promptTokens, completionTokens))
                    } else {
                        emit(AIResponse("Ошибка: нет ответа от API", promptTokens, 0))
                    }
                } else {
                    emit(AIResponse("Ошибка API: ${response.code}\nПроверьте API ключ", 0, 0))
                }
            }
        } catch (e: Exception) {
            emit(AIResponse("Ошибка: ${e.message}\nПроверте интернет и API ключ", 0, 0))
        }
    }

    private fun <T> Flow<T>.emitAll(flow: Flow<T>): Flow<T> = flow {
        flow.collect { emit(it) }
    }
}
