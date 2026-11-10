package com.analplug.app.data.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.analplug.app.data.entity.Plug
import com.analplug.app.data.entity.PlugTagCrossRef
import com.analplug.app.data.entity.Tag
import com.analplug.app.data.entity.WearSession

data class PlugWithTags(
    @Embedded val plug: Plug,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PlugTagCrossRef::class,
            parentColumn = "plugId",
            entityColumn = "tagId"
        )
    )
    val tags: List<Tag>
)

data class PlugWithHistory(
    @Embedded val plug: Plug,
    @Relation(
        parentColumn = "id",
        entityColumn = "plugId"
    )
    val sessions: List<WearSession>
)

data class WearSessionWithPlug(
    @Embedded val session: WearSession,
    @Relation(
        parentColumn = "plugId",
        entityColumn = "id"
    )
    val plug: Plug
)
