package dev.kord.core.event.guild

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.GuildBehavior
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Strategizable
import dev.kord.core.event.Event
import dev.kord.core.supplier.EntitySupplier
import dev.kord.core.supplier.EntitySupplyStrategy
import kotlin.coroutines.CoroutineContext

class IntegrationsUpdateEvent(
    val guildId: Snowflake,
    override val kord: Kord,
    override val shard: Int,
    override val supplier: EntitySupplier = kord.defaultSupplier,
    override val coroutineContext: CoroutineContext = kord.coroutineContext,
) : Event, Strategizable {

    val guild: GuildBehavior get() = GuildBehavior(guildId, kord)

    suspend fun getGuild(): Guild = supplier.getGuild(guildId)

    suspend fun getGuildOrNull(): Guild? = supplier.getGuildOrNull(guildId)

    override fun withStrategy(strategy: EntitySupplyStrategy<*>): IntegrationsUpdateEvent =
        IntegrationsUpdateEvent(guildId, kord, shard, strategy.supply(kord))

    override fun toString(): String {
        return "IntegrationsUpdateEvent(guildId=$guildId, kord=$kord, shard=$shard, supplier=$supplier)"
    }
}
