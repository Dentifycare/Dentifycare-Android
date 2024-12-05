package com.dentify.dentifycare.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dentify.dentifycare.data.local.UserPreferences
import com.dentify.dentifycare.data.remote.retrofit.ApiConfig
import com.dentify.dentifycare.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class DentifyRepository private constructor(
    private var apiService: ApiService,
    private val userPreferences: UserPreferences
){
    fun registerPatient(name: String, email: String, password: String, phone: String): LiveData<Result<RegisterPatientResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.registerPatient(name, email, password, phone)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterPatientResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun registerCoAss(name: String, email: String, password: String, phone: String, university: String, semester: String, studentId: String): LiveData<Result<RegisterCoAssResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.registerCoAss(name, email, password, phone, university, semester, studentId)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterCoAssResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun loginUser(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.loginUser(email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getListNews(): LiveData<Result<NewsResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getListNews()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, NewsResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun clearUserToken() {
        userPreferences.clearToken()
    }

    fun saveUserToken(token: String) {
        userPreferences.saveToken(token)
        apiService = ApiConfig.getApiService(token)
    }

    companion object {
        @Volatile
        private var instance: DentifyRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): DentifyRepository =
            instance ?: synchronized(this) {
                instance ?: DentifyRepository(apiService, userPreferences)
            }.also { instance = it }
    }
}