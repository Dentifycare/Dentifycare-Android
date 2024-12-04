package com.dentify.dentifycare.data

import com.dentify.dentifycare.data.local.UserPreferences
import com.dentify.dentifycare.data.remote.retrofit.ApiConfig
import com.dentify.dentifycare.data.remote.retrofit.ApiService

class DentifyRepository private constructor(
    private var apiService: ApiService,
    private val userPreferences: UserPreferences
){

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