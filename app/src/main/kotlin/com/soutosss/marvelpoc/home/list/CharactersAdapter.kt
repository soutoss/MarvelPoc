package com.soutosss.marvelpoc.home.list

import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.home.list.CharactersAdapter.CharacterHomeViewHolder
import com.soutosss.marvelpoc.widget.CharacterView

class CharactersAdapter(
    private val favoriteClick: (Character) -> Unit,
    private val itemClick: (Character) -> Unit
) : PagedListAdapter<Character, CharacterHomeViewHolder>(CharacterDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterHomeViewHolder {
        val characterView = CharacterView(parent.context)
        characterView.setPadding(1, 1, 1, 1)
        characterView.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        return CharacterHomeViewHolder(characterView)
    }

    override fun onBindViewHolder(holder: CharacterHomeViewHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }

    inner class CharacterHomeViewHolder(private val characterView: CharacterView) : RecyclerView.ViewHolder(characterView) {

        fun bind(character: Character) {
            characterView.updateCharacter(character)
        }

        init {
            characterView.setListeners(favoriteClick, itemClick)
        }
    }

    class CharacterDiff : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Character, newItem: Character) =
            oldItem == newItem
    }

}