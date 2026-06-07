package io.github.josephsanjaya.kautoservice.plugin

import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration

class KAutoServiceCommandLineProcessor : CommandLineProcessor {
    override val pluginId = "io.github.josephsanjaya.kautoservice"

    override val pluginOptions: Collection<AbstractCliOption> = listOf(
        CliOption("enabled", "<true|false>", "Enable KAutoService plugin", required = false),
        CliOption("verify", "<true|false>", "Enable compile-time type verification", required = false),
        CliOption("verbose", "<true|false>", "Enable verbose logging", required = false)
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option.optionName) {
            "enabled" -> configuration.put(KAutoServiceConfigurationKeys.ENABLED, value.toBooleanStrict())
            "verify" -> configuration.put(KAutoServiceConfigurationKeys.VERIFY, value.toBooleanStrict())
            "verbose" -> configuration.put(KAutoServiceConfigurationKeys.VERBOSE, value.toBooleanStrict())
        }
    }
}
