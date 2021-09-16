package dev.kord.core.gateway

import dev.kord.gateway.Command
import dev.kord.gateway.Gateway
import dev.kord.gateway.GatewayConfiguration
import dev.kord.gateway.GatewayConfigurationBuilder
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

interface MasterGateway {
    val gateways: Map<Int, Gateway>

    /**
     * Calculates the average [Gateway.ping] of all running [gateways].
     *
     * Gateways that return `null` are not counted into the average, if all [gateways]
     * return `null` then this property will return `null` as well.
     */
    @OptIn(ExperimentalTime::class)
    val averagePing: Duration?


    @OptIn(FlowPreview::class)
    val events: Flow<ShardEvent>

    suspend fun startWithConfig(configuration: GatewayConfiguration): Unit = coroutineScope {
        gateways.entries.forEach { (shard, gateway) ->
            val config = configuration.copy(shard = configuration.shard.copy(index = shard))
            launch {
                gateway.start(config)
            }
        }
    }

    suspend fun sendAll(command: Command) = gateways.values.forEach { it.send(command) }

    suspend fun detachAll() = gateways.values.forEach { it.detach() }

    suspend fun stopAll() = gateways.values.forEach { it.stop() }
}

suspend inline fun MasterGateway.start(token: String, config: GatewayConfigurationBuilder.() -> Unit = {}) {
    val builder = GatewayConfigurationBuilder(token)
    builder.apply(config)
    startWithConfig(builder.build())
}