package net.hugebot.presencesbot.listeners

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.hugebot.presencesbot.Launcher
import net.hugebot.presencesbot.tryOrNull
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class DiscordListeners(private val guildId: String, private val channelId: String? = null) : ListenerAdapter() {

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

            val voiceChannel = if (channelId != null) getVoiceChannel(guild, channelId) else null

            log.info("Getting RPCs from ${members.size} users within \"${guild.name}\"...")

            val presences = members.mapNotNull { m ->
                m.activities.find {
                    ac -> ac.isRich && ac.asRichPresence()!!.applicationId == GAME_ID || ac.isRich && ac.asRichPresence()!!.name == GAME_NAME
                }
            }.count()

            if (lastPresence == null || presences != lastPresence) {
                log.info("$presences RPCs have been obtained from users playing $GAME_NAME in \"${guild.name}\", updating presence...")
                Launcher.setActivity(Activity.playing(buildPresenceTemplate(presences)))

                if (voiceChannel != null) {
                    when {
                        guild.selfMember.hasPermission(voiceChannel, Permission.MANAGE_CHANNEL) -> {
                            val name = buildChannelTemplate(presences)
                            updateVoiceChannelName(voiceChannel, name)
                        }
                        else -> log.warn("Not permissions for change text channel name.")
                    }
                }
            } else {
                log.info("$presences RPCs have been obtained from users playing $GAME_NAME in \"${guild.name}\", not updating because the old count was equal...")
            }
            lastPresence = presences
        }
    }

    private fun getVoiceChannel(guild: Guild, channelId: String): VoiceChannel? {
        val c = guild.getVoiceChannelById(channelId)

        if (c == null) log.warn("Cannot get VoiceChannel for ID $channelId")
        else log.info("Got VoiceChannel $c")
        return c
    }

    private fun buildPresenceTemplate(count: Int): String {
        return PRESENCE_TEMPLATE.replace("{game}", GAME_NAME, true).replace("{count}", count.toString(), true)
    }

    private fun buildChannelTemplate(count: Int): String {
        return CHANNEL_TEMPLATE.replace("{game}", GAME_NAME, true).replace("{count}", count.toString(), true)
    }

    private fun updateVoiceChannelName(voiceChannel: VoiceChannel, name: String) {
        return voiceChannel.manager.setName(name).queue({
            log.info("Updated channel name!!")
        }, {
            log.error("Cannot update channel name: ${it.message}")
        })
    }

    companion object {
        private val log = LoggerFactory.getLogger(DiscordListeners::class.java)

        private val GAME_ID = tryOrNull { System.getenv("GAME_ID") } ?: "746966631686733855"
        private val GAME_NAME = tryOrNull { System.getenv("GAME_NAME") } ?: "Among Us"
        private val PRESENCE_TEMPLATE = tryOrNull { System.getenv("PRESENCE_TEMPLATE") } ?: "{game} with {count} members!!"
        private val CHANNEL_TEMPLATE = tryOrNull { System.getenv("CHANNEL_TEMPLATE") } ?: "{game}: {count}"
    }
}