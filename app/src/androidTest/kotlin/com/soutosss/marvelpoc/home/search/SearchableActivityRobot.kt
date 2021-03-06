package com.soutosss.marvelpoc.home.search

import android.app.SearchManager
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.home.HomeViewModel
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.inject
import org.koin.dsl.module

fun configure(func: SearchableActivityConfiguration.() -> Unit) =
    SearchableActivityConfiguration().apply(func)

class SearchableActivityConfiguration : KoinComponent {
    private val mockRepository: CharactersRepository = mockk(relaxed = true)
    private val homeViewModel: HomeViewModel = HomeViewModel(mockRepository)
    private lateinit var intent: Intent

    fun withSearchableIntent() {
        intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            SearchableActivity::class.java
        )
        intent.action = Intent.ACTION_SEARCH
        intent.putExtra(SearchManager.QUERY, "ops")
    }

    fun withIntentNotSearchable() {
        intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            SearchableActivity::class.java
        )
    }

    infix fun launch(func: SearchableActivityRobot.() -> Unit): SearchableActivityRobot {
        loadKoinModules(
            module(override = true) {
                single { mockRepository }
                single { spyk(homeViewModel) }
            })

        ActivityScenario.launch<SearchableActivity>(intent)
        return SearchableActivityRobot().apply(func)
    }
}

class SearchableActivityRobot {
    infix fun check(func: SearchableActivityResult.() -> Unit) =
        SearchableActivityResult().apply(func)
}

class SearchableActivityResult : KoinComponent {
    private val viewModel: HomeViewModel by inject()

    fun callViewModelWithExpectedContent() {
        verify { viewModel.initSearchQuery("ops") }
    }

    fun notCallViewModel() {
        verify (exactly = 0){ viewModel.initSearchQuery(any()) }
    }
}
