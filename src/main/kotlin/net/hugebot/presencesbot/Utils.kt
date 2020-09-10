package net.hugebot.presencesbot

inline fun <R> tryOrNull(crossinline block: () -> R): R? {
    return try {
        block()
    } catch (e: Throwable) {
        return null
    }
}
