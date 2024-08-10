package com.zenitsu.ceritakita.data

import android.util.Log
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.zenitsu.ceritakita.data.pref.UserModel
import com.zenitsu.ceritakita.data.pref.UserPreference
import com.zenitsu.ceritakita.data.remote.response.Response
import com.zenitsu.ceritakita.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import com.zenitsu.ceritakita.utils.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            val token = response.loginResult?.token ?: ""
            Log.d(TAG, "UserModel status: $email $token isLogin=true")
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, Response::class.java)
            Log.d(TAG, "response: ${errorResponse.message}")
            emit(errorResponse.message?.let { Result.Error(it) })
        } catch (e: Exception) {
            Log.e(TAG, "response: ${e.message}")
            emit(Result.Error("An unexpected error occurred. ${e.message}"))
        }
    }

    fun register(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.register(name, email, password)
            Log.d(TAG, "response: $successResponse")
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, Response::class.java)
            Log.d(TAG, "$errorResponse")
            emit(errorResponse.message?.let { Result.Error(it) })
        } catch (e: Exception) {
            Log.e(TAG, "response: ${e.message}")
            emit(Result.Error("An unexpected error occurred. ${e.message}"))
        }
    }

    fun getStories() = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getStories()
            Log.d(TAG, "$successResponse")
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, Response::class.java)
            Log.d(TAG, "$errorResponse")
            emit(errorResponse.message?.let { Result.Error(it) })
        } catch (e: Exception) {
            Log.e(TAG, "response: ${e.message}")
            emit(Result.Error("An unexpected error occurred. ${e.message}"))
        }
    }

    fun uploadStory(imageFile: File, description: String) = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadStory(multipartBody, requestBody)
            Log.d(TAG, "$successResponse")
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, Response::class.java)
            Log.d(TAG, "$errorResponse")
            emit(errorResponse.message?.let { Result.Error(it) })
        } catch (e: Exception) {
            Log.e(TAG, "response: ${e.message}")
            emit(Result.Error("An unexpected error occurred. ${e.message}"))
        }
    }


    companion object {
        private const val TAG = "UserRepository"
        fun getInstance(userPreference: UserPreference, apiService: ApiService): UserRepository =
            UserRepository(userPreference, apiService)
    }
}

