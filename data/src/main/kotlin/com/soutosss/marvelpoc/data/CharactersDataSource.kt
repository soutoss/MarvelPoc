package com.soutosss.marvelpoc.data

import androidx.paging.PositionalDataSource
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.data.network.CharactersApi
import com.soutosss.marvelpoc.data.network.character.toCharacterList
import com.soutosss.marvelpoc.data.room_source.CharacterLocal
import com.soutosss.marvelpoc.shared.contracts.character.CharacterLocalContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CharactersDataSource(
    private val queryText: String?,
    private val scope: CoroutineScope,
    private val api: CharactersApi,
    private val localDataSource: CharacterLocalContract<CharacterLocal>,
    private val exceptionHandler: (Exception) -> Unit,
    private val loadFinished: () -> Unit
) :
    PositionalDataSource<Character>() {

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<Character>
    ) {
        scope.launch {
            try {
                val response = api.listCharacters(
                    name = queryText,
                    offset = 0,
                    limit = params.requestedLoadSize
                ).toCharacterList()
                if (response.isEmpty()) {
                    throw EmptyDataException()
                }
                callback.onResult(response.checkFavorite(), 0)
                loadFinished()
            } catch (e: Exception) {
                exceptionHandler(e)
            }
        }
    }

    private suspend fun List<Character>.checkFavorite(): List<Character> {
        val favorites = localDataSource.favoriteIds()
        favorites.forEach { id ->
            this.firstOrNull { it.id == id }?.favorite = true
        }
        return this
    }

    override fun loadRange(
        params: LoadRangeParams,
        callback: LoadRangeCallback<Character>
    ) {
        scope.launch {
            try {
                val response =
                    api.listCharacters(
                        name = queryText,
                        offset = params.startPosition,
                        limit = params.loadSize
                    )
                callback.onResult(response.toCharacterList().checkFavorite())
            } catch (e: Exception) {
                exceptionHandler(e)
            }
        }
    }
}
