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
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import javax.security.auth.login.LoginException

object Launcher {
    internal val log = LoggerFactory.getLogger(Launcher::class.java)
    internal val scheduler = Executors.newSingleThreadScheduledExecutor()

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
                .enableCache(
                        CacheFlag.MEMBER_OVERRIDES,
                        CacheFlag.ACTIVITY
                )
                .enableIntents(
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_PRESENCES
                )
                .build()

        //  Añadimos un ShutdownHook para cerrar de manera correcta los procesos de la aplicación
        Runtime.getRuntime().addShutdownHook(Thread {
            log.info("Shutting down shard manager...")
            shardManager.shutdown()
        })
    }

    inline fun schedulerAtFixedRate(
            scheduler: ScheduledExecutorService,
            logger: Logger,
            initialDelay: Long,
            period: Long,
            unit: TimeUnit,
            crossinline block: () -> Unit
    ): ScheduledFuture<*> = scheduler.scheduleAtFixedRate({
            try {
                block()
            } catch (e: Throwable) {
                logger.error("Error executing scheduled task: ${e.stackTraceToString()}")
            }
        }, initialDelay, period, unit)
    }
