package net.hugebot.presencesbot.listeners

import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.hugebot.presencesbot.Launcher
import net.hugebot.presencesbot.tryOrNull
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class DiscordListeners(private val guildId: String) : ListenerAdapter() {

    private var lastPresence: Int? = null

    override fun onGuildJoin(event: GuildJoinEvent) {
        if (event.guild.id != guildId) {
            log.info("Leaving guild ${event.guild.name}")
            event.guild.leave().queue()
        }
    }

    override fun onReady(event: ReadyEvent) {
        Launcher.scheduleAtFixedRate(Launcher.scheduler, log, 5, 300, TimeUnit.SECONDS) {

            val guild = Launcher.getGuildById(guildId)!!
            val members = guild.loadMembers().get()

            log.info("Getting RPCs from ${members.size} users within \"${guild.name}\"...")

            val presences = members.mapNotNull { m ->
                m.activities.find {
                    ac -> ac.isRich && ac.asRichPresence()!!.applicationId == GAME_ID || ac.isRich && ac.asRichPresence()!!.name == GAME_NAME
                }
            }.count()

            if (lastPresence == null || presences != lastPresence) {
                log.info("$presences RPCs have been obtained from users playing $GAME_NAME in \"${guild.name}\", updating presence...")
                Launcher.setActivity(Activity.playing(PRESENCE_TEMPLATE
                        .replace("{game}", GAME_NAME, true)
                        .replace("{count}", presences.toString(), true)))
            } else {
                log.info("$presences RPCs have been obtained from users playing $GAME_NAME in \"${guild.name}\", not updating because the old count was equal...")
            }
            lastPresence = presences
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(DiscordListeners::class.java)

        private val GAME_ID = tryOrNull { System.getenv("GAME_ID") } ?: "746966631686733855"
        private val GAME_NAME = tryOrNull { System.getenv("GAME_NAME") } ?: "Among Us"
        private val PRESENCE_TEMPLATE = tryOrNull { System.getenv("PRESENCE_TEMPLATE") } ?: "{game} with {count} members!!"
    }
}