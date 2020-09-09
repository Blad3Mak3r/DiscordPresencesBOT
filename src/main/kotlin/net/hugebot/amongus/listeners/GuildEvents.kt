package net.hugebot.amongus.listeners

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.PrivateChannel
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.restaction.MessageAction
import java.awt.Color
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
            setAuthor(event.jda.selfUser.name, event.guild.vanityUrl, event.jda.selfUser.effectiveAvatarUrl)
            setColor(Color.red)
            setTitle("Bienvenido a **${event.guild.name}**")
            appendDescription("**${event.user.name}** estamos muy agradecidos de que hayas decidido unirte a la mayor comunidad de Among Us en español, " +
                    "esperemos que lo pases en grande jugando con nosotros.")
            addBlankField(false)
            addField("❯ Información", buildString {
                appendLine("Lo primero de todo lee nuestro [canal de información](${INFO_TEXT_CHANNEL}) para saber todo lo necesario sobre el servidor.")
            },false)
            addField("❯ Reglas", buildString {
                appendLine("Lee [las normas](${RULES_TEXT_CHANNEL}) de nuestro servidor.")
            }, false)
            addField("❯ Sugerencias", buildString {
                appendLine("Puedes enviarnos tus [sugerencias](${SUGGESTIONS_TEXT_CHANNEL}) para mejorar la comunidad ♥.")
            }, false)
            addField("❯ Necesito ayuda", buildString {
                appendLine("En caso de que necesites ayuda de cualquier tipo, no dudes en [escribirnos](${SUGGESTIONS_TEXT_CHANNEL})!!")
            }, false)
            addField("❯ Tengo un problema...", buildString {
                appendLine("No dudes en leer [nuestro canal](${ISSUES_TEXT_CHANNEL}) dedicado para esto.")
            }, false)
            addField("❯ Y por último pero no menos important", buildString {
                appendLine("Visita nuestras [redes sociales](${SOCIAL_TEXT_CHANNEL}).")
            }, false)
            setThumbnail(event.guild.iconUrl)
            setImage("https://media1.tenor.com/images/ef4993b593954811a0c0a1c98af698a3/tenor.gif?itemid=16399941")
            setFooter("¡¡Esperamos que lo pases en grande con nosotros!!")
        }

        return channel.sendMessage(embed.build())
    }

    companion object {
        const val RULES_TEXT_CHANNEL = "https://discordapp.com/channels/619457585241653248/753033046646718524"
        const val INFO_TEXT_CHANNEL = "https://discordapp.com/channels/619457585241653248/751795851961303061"
        const val SUGGESTIONS_TEXT_CHANNEL = "https://discordapp.com/channels/619457585241653248/752513733556830228"
        const val ISSUES_TEXT_CHANNEL = "https://discordapp.com/channels/619457585241653248/753013605472927855"
        const val SOCIAL_TEXT_CHANNEL = "https://discordapp.com/channels/619457585241653248/751811217793876092"
    }
}