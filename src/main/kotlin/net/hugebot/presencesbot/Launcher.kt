package net.hugebot.presencesbot

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.utils.cache.CacheFlag
import net.hugebot.presencesbot.listeners.DiscordListeners
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import javax.security.auth.login.LoginException

object Launcher {
    private val log = LoggerFactory.getLogger(Launcher::class.java)
    internal val scheduler = Executors.newSingleThreadScheduledExecutor()

    lateinit var jda: JDA
        private set


    @Throws(LoginException::class, IllegalArgumentException::class)
    @ExperimentalStdlibApi
    @JvmStatic
    fun main(args: Array<String>) {
        Thread.currentThread().name = "DiscordPresences-MAIN"
        System.setProperty("java.net.preferIPv4Stack", "true")

        //  Obtenemos el token desde los argumentos de ejecución o desde las variables de entorno
        log.info("Getting Discord Bot Token...")
        val token = tryOrNull { args[0] } ?: System.getenv("DISCORD_TOKEN")

        log.info("Getting Guild ID...")
        val guildId = tryOrNull { args[1] } ?: System.getenv("GUILD_ID")

        RestAction.setPassContext(false)
        RestAction.setDefaultFailure { }

        log.info("Starting JDA instance...")

        jda = JDABuilder.createLight(token)
                .addEventListeners(DiscordListeners(guildId))
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
            log.info("Shutting down JDA instance...")
            jda.shutdown()
        })
    }

    fun getGuildById(id: String) = jda.getGuildById(id)
    fun getGuildById(id: Long) = jda.getGuildById(id)

    fun setActivity(activity: Activity) {
        jda.presence.activity = activity
    }

    inline fun scheduleAtFixedRate(
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
