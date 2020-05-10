package com.soutosss.marvelpoc.data.network

import com.soutosss.marvelpoc.data.model.character.MarvelCharactersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersApi {
    @GET("characters")
    suspend fun listCharacters(@Query("nameStartsWith") name: String? = null): MarvelCharactersResponse
}