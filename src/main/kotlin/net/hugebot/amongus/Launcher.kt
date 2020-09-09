package net.hugebot.amongus

import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException
import javax.security.auth.login.LoginException

object Launcher {
    private val log = LoggerFactory.getLogger(Launcher::class.java)
    private val token = System.getenv("DISCORD_TOKEN")

    lateinit var shardManager: ShardManager
        private set


    @Throws(LoginException::class, IllegalArgumentException::class)
    @ExperimentalStdlibApi
    @JvmStatic
    fun main(args: Array<String>) {
        Thread.currentThread().name = "AmongUs-Main"
        System.setProperty("java.net.preferIPv4Stack", "true")

        RestAction.setPassContext(false)
        RestAction.setDefaultFailure { }

        log.info("Iniciando bot...")
        shardManager = DefaultShardManagerBuilder.createLight(token)
                .setActivity(Activity.playing("Among Us"))
                .setEnableShutdownHook(true)
                .build()

    }
}