package net.hugebot.presencesbot.listeners

import com.xenomachina.argparser.ArgParser

class ApplicationArguments(parser: ArgParser) {
    val token = parser.storing("-T", "--token", help = "Discord Bot Token") { toString() }
    val guildId = parser.storing("-G", "--guild", help = "A valid Guild ID") { toString() }
}