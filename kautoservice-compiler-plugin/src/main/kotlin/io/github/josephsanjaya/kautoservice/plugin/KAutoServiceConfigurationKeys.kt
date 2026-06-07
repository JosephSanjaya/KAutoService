package io.github.josephsanjaya.kautoservice.plugin

import org.jetbrains.kotlin.config.CompilerConfigurationKey

object KAutoServiceConfigurationKeys {
    val ENABLED = CompilerConfigurationKey<Boolean>("enable processing")
    val VERIFY = CompilerConfigurationKey<Boolean>("verify implementations")
    val VERBOSE = CompilerConfigurationKey<Boolean>("verbose logging")
}
