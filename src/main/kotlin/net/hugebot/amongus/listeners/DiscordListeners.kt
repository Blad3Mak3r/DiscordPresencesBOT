package net.hugebot.amongus.listeners

import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.hugebot.amongus.Launcher
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class DiscordListeners : ListenerAdapter() {

    override fun onReady(event: ReadyEvent) {
        Launcher.schedulerAtFixedRate(Launcher.scheduler, log, 5, 300, TimeUnit.SECONDS) {
            log.info("Obteniendo RPCs de usuarios...")

            val guild = Launcher.shardManager.getGuildById(Launcher.guildId)!!
            val members = guild.loadMembers().get()

            log.info("Se han obtenido un total de ${members.size} miembros de ${guild.name}")

            val presences = members.mapNotNull { m ->
                m.activities.find {
                    ac -> ac.isRich && ac.asRichPresence()!!.applicationIdLong == AMONGUS_APPID || ac.isRich && ac.asRichPresence()!!.name == "Among Us"
                }
            }.count()

            log.info("Se han obtenido $presences RPCs de usuarios jugando Among Us")
            Launcher.shardManager.setActivity(Activity.playing(if (presences > 0) "Among Us con $presences miembros!!" else "Among Us"))
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(DiscordListeners::class.java)

        private val AMONGUS_APPID = 746966631686733855L
    }
}