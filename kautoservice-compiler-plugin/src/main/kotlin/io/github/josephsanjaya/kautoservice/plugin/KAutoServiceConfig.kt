package io.github.josephsanjaya.kautoservice.plugin

import org.jetbrains.kotlin.cli.common.messages.MessageCollector

object KAutoServiceConfig {
    private val threadCollector = InheritableThreadLocal<MessageCollector?>()
    private val threadVerbose = InheritableThreadLocal<Boolean?>()

    var verbose: Boolean
        get() = threadVerbose.get() ?: false
        set(value) = threadVerbose.set(value)

    var messageCollector: MessageCollector
        get() = threadCollector.get() ?: MessageCollector.NONE
        set(value) = threadCollector.set(value)

    fun clear() {
        threadCollector.remove()
        threadVerbose.remove()
    }
}
