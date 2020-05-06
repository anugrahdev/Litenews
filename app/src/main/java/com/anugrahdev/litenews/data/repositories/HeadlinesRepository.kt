package com.anugrahdev.litenews.data.repositories

import com.anugrahdev.litenews.data.network.ApiService
import com.anugrahdev.litenews.data.network.SafeApiRequest

class HeadlinesRepository(private val api:ApiService):SafeApiRequest() {

    suspend fun getHeadlines() = apiRequest { api.getTopHeadlines("id"," ") }
    suspend fun getHeadlinesTechnology() = apiRequest { api.getTopHeadlines("id","technology") }
    suspend fun getHeadlinesSports()=apiRequest { api.getTopHeadlines("id","sports") }
    suspend fun getHeadlinesScience()= apiRequest { api.getTopHeadlines("id","science") }
    suspend fun getHeadlinesBusiness() = apiRequest { api.getTopHeadlines("id","business") }
    suspend fun getHeadlineshealth()  = apiRequest { api.getTopHeadlines("id","health") }
    suspend fun getHeadlinesEntertainment() = apiRequest { api.getTopHeadlines("id","entertainment") }
}