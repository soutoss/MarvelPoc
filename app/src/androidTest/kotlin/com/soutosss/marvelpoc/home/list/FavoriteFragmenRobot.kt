package com.soutosss.marvelpoc.home.list

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.character.CharacterLocalContract
import com.soutosss.marvelpoc.data.model.view.Character
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.Matchers.not
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun configureFavorite(func: FavoriteFragmentConfiguration.() -> Unit) =
    FavoriteFragmentConfiguration().apply(func)

class FavoriteFragmentConfiguration : KoinComponent {
    private val localSource: CharacterLocalContract<Character> = mockk(relaxed = true)
    private val repository: CharactersRepository = CharactersRepository(mockk(), mockk())
    infix fun launch(func: FavoriteFragmentRobot.() -> Unit): FavoriteFragmentRobot {
        loadKoinModules(
            module(override = true) {
                single { localSource }
                single { repository }
            })

        launchFragmentInContainer<FavoriteFragment>()
        return FavoriteFragmentRobot().apply(func)
    }


    fun withNotEmptyList() {
        every { repository.fetchFavoriteCharacters() } returns FakeHomeDataSource(
            listOf(
                Character(
                    30,
                    "3-D Test HAHAH",
                    "http://www.google.com",
                    "description",
                    true
                )
            )
        )
    }

    fun withNoFavorites() {
        every { repository.fetchFavoriteCharacters() } returns FakeHomeDataSource(emptyList())
    }

}

class FavoriteFragmentRobot {
    infix fun check(func: FavoriteFragmentResult.() -> Unit) =
        FavoriteFragmentResult().apply(func)

}

class FavoriteFragmentResult {

    fun characterFavoriteName() {
        checkCharacterName("3-D Test HAHAH")
    }

    private fun checkCharacterName(characterName: String) {
        onView(withId(R.id.recycler)).check(matches(hasDescendant(withText(characterName))))
    }


    private fun checkErrorMessage(message: String) {
        onView(withId(R.id.message)).check(matches(isDisplayed()))
        onView(withId(R.id.erroIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.message)).check(matches(withText(message)))
    }

    fun recyclerViewIsHidden() {
        onView(withId(R.id.recycler)).check(matches(not(isDisplayed())))
    }

    fun checkFavoritesEmptyMessage() =
        checkErrorMessage("You don't have favorite marvel character :(")
}
