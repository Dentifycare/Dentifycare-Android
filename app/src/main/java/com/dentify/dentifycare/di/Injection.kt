package com.dentify.dentifycare.di

import android.content.Context
import com.dentify.dentifycare.data.DentifyRepository
import com.dentify.dentifycare.data.local.UserPreferences
import com.dentify.dentifycare.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): DentifyRepository {
        val pref = UserPreferences.getInstance(context)
        val user = pref.getUserToken()
        val apiService = ApiConfig.getApiService(user)
        return DentifyRepository.getInstance(apiService, pref)
    }
}