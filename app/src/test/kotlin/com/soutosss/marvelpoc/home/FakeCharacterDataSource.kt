package com.soutosss.marvelpoc.home

import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.soutosss.marvelpoc.data.model.view.Character

class FakeCharacterDataSource(
    private val items: List<Character>,
    private val exception: Exception? = null
) :
    DataSource.Factory<Int, Character>() {
    override fun create(): DataSource<Int, Character> = FakeDataSource(items, exception)
}

class FakeDataSource<T>(var items: List<T>, private val exception: Exception? = null) :
    PositionalDataSource<T>() {
    override fun loadRange(
        params: LoadRangeParams,
        callback: LoadRangeCallback<T>
    ) {
        if (exception != null) {
            throw exception
        }
        callback.onResult(
            items.subList(
                params.startPosition,
                params.startPosition + params.loadSize
            )
        )
    }

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<T>
    ) {
        val totalCount = items.size

        val position = computeInitialLoadPosition(params, totalCount)
        val loadSize = computeInitialLoadSize(params, position, totalCount)

        val sublist = items.subList(position, position + loadSize)
        callback.onResult(sublist, position, totalCount)
    }

}
