package net.hugebot.presencesbot

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.RichPresence
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import net.hugebot.presencesbot.listeners.DiscordListeners
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val token = args[0]
    val argGuildId = args[1]

    val jda = JDABuilder.createLight(token)
        .addEventListeners(GetPresences(argGuildId))
        .enableCache(
            CacheFlag.ACTIVITY,
            CacheFlag.MEMBER_OVERRIDES
        )
        .enableIntents(
            GatewayIntent.GUILD_PRESENCES,
            GatewayIntent.GUILD_MEMBERS
        )
        .build()

    Runtime.getRuntime().addShutdownHook(Thread {
        jda.shutdown()
    })
}

class GetPresences(private val guildId: String) : ListenerAdapter() {

    override fun onReady(event: ReadyEvent) {
        val t = Executors.defaultThreadFactory().newThread {
            val guild = event.jda.getGuildById(guildId)!!
            val members = guild.loadMembers().get().toSet()

            log.info("Getting RPCs from ${members.size} users within \"${guild.name}\"...")

            val presences = mutableSetOf<RichPresence>()
            members.forEach { m -> presences.addAll(m.activities.mapNotNull { a -> a.asRichPresence() }) }

            log.info(buildString {
                appendLine("Found ${presences.size} activities with Rich Presence content: ")
                presences.forEach {
                    appendLine("    • $it")
                }
            })

            exitProcess(1)
        }

        t.start()
    }

    companion object {
        private val log = LoggerFactory.getLogger(GetPresences::class.java)
    }
}