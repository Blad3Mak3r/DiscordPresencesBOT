package net.hugebot.amongus

import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.utils.cache.CacheFlag
import net.hugebot.amongus.listeners.GuildEvents
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException
import javax.security.auth.login.LoginException

object Launcher {
    private val log = LoggerFactory.getLogger(Launcher::class.java)

    lateinit var token: String
        private set

    lateinit var shardManager: ShardManager
        private set


    @Throws(LoginException::class, IllegalArgumentException::class)
    @ExperimentalStdlibApi
    @JvmStatic
    fun main(args: Array<String>) {
        Thread.currentThread().name = "AmongUs-Main"
        System.setProperty("java.net.preferIPv4Stack", "true")

        //  Obtenemos el token desde los argumentos de ejecución o desde las variables de entorno
        log.info("Getting Discord Bot Token...")
        token = tryOrNull { args[0] } ?: System.getenv("DISCORD_TOKEN")

        RestAction.setPassContext(false)
        RestAction.setDefaultFailure { }

        log.info("Starting ShardManager...")
        shardManager = DefaultShardManagerBuilder.createLight(token)
                /*.addEventListeners(GuildEvents())*/
                .enableCache(CacheFlag.MEMBER_OVERRIDES)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build()

        //  Añadimos un ShutdownHook para cerrar de manera correcta los procesos de la aplicación
        Runtime.getRuntime().addShutdownHook(Thread {
            log.info("Shutting down shard manager...")
            shardManager.shutdown()
        })
    }
}