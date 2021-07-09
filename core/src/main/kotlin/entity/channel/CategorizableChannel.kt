package dev.kord.core.entity.channel

import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.value
import dev.kord.core.behavior.channel.CategoryBehavior
import dev.kord.core.cache.data.InviteData
import dev.kord.core.entity.Invite
import dev.kord.core.toSnowflakeOrNull
import dev.kord.rest.builder.channel.InviteCreateBuilder
import dev.kord.rest.request.RestRequestException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * An instance of a Discord channel associated to a [category].
 */
interface CategorizableChannel : GuildChannel {

    /**
     * The id of the [category] this channel belongs to, if any.
     */
    val categoryId: Snowflake?
        get() = data.parentId.value

    /**
     * The category behavior this channel belongs to, if any.
     */
    val category: CategoryBehavior?
        get() = when (val categoryId = categoryId) {
            null -> null
            else -> CategoryBehavior(id = categoryId, guildId = guildId, kord = kord)
        }


}

