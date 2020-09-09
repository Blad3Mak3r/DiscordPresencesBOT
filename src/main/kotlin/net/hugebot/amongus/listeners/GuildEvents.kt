package net.hugebot.amongus.listeners

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.PrivateChannel
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.restaction.MessageAction
import java.time.OffsetDateTime

class GuildEvents : ListenerAdapter() {

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        if (event.member.timeJoined.isBefore(OffsetDateTime.now().minusSeconds(30))) return

        event.user.openPrivateChannel().flatMap {
            buildWelcomeMessage(it, event)
        }.queue()

    }

    private fun buildWelcomeMessage(channel: PrivateChannel, event: GuildMemberJoinEvent): MessageAction {
        val embed = EmbedBuilder().apply {
            setAuthor("AmongUsEs", null, event.jda.selfUser.effectiveAvatarUrl)
            setTitle("Bienvenido a **AmongUsEs**")
            appendDescription("**${event.user.name}** estamos muy agradecidos de que hayas decidido unirte a la mayor comunidad de Among Us en español," +
                    "esperemos que lo pases en grande jugando con nosotros.")
            appendDescription("\n")
            appendDescription("Acuerdate de seguir siempre **[las normas](${RULES_TEXT_CHANNEL})** de nuestro servidor")
            setThumbnail(event.guild.iconUrl)
            setImage("https://media1.tenor.com/images/ef4993b593954811a0c0a1c98af698a3/tenor.gif?itemid=16399941")
        }

        return channel.sendMessage(embed.build())
    }

    companion object {
        const val RULES_TEXT_CHANNEL = "https://discordapp.com/channels/619457585241653248/753033046646718524"
    }
}