package com.soutosss.marvelpoc.data.room_source.ext

import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.data.room_source.CharacterLocal

fun Character.toCharacterLocal() =
    CharacterLocal(
        this.id,
        this.name,
        this.thumbnailUrl,
        this.description,
        this.favorite
    )