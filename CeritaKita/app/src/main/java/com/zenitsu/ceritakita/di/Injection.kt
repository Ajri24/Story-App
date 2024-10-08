package com.zenitsu.ceritakita.di

import android.content.Context
import com.zenitsu.ceritakita.data.UserRepository
import com.zenitsu.ceritakita.data.pref.UserPreference
import com.zenitsu.ceritakita.data.pref.dataStore
import com.zenitsu.ceritakita.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking {
            pref.getSession().first()
        }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(pref, apiService)
    }
}