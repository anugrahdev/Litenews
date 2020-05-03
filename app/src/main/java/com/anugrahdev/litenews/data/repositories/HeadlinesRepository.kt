package com.anugrahdev.litenews.data.repositories

import com.anugrahdev.litenews.data.network.ApiService
import com.anugrahdev.litenews.data.network.SafeApiRequest

class HeadlinesRepository(private val api:ApiService):SafeApiRequest() {

    suspend fun getHeadlines() = apiRequest { api.getTopHeadlines("id") }

}