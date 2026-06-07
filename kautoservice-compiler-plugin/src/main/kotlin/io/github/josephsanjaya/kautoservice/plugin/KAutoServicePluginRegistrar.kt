package io.github.josephsanjaya.kautoservice.plugin

import io.github.josephsanjaya.kautoservice.plugin.fir.KAutoServiceFirRegistrar
import io.github.josephsanjaya.kautoservice.plugin.ir.KAutoServiceIrExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter

class KAutoServicePluginRegistrar : CompilerPluginRegistrar() {
    override val supportsK2 = true
    override val pluginId = "io.github.josephsanjaya.kautoservice"

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val enabled = configuration.get(KAutoServiceConfigurationKeys.ENABLED, true)
        if (!enabled) return

        val verify = configuration.get(KAutoServiceConfigurationKeys.VERIFY, true)
        val verbose = configuration.get(KAutoServiceConfigurationKeys.VERBOSE, false)
        val messageCollector = configuration.get(CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)

        KAutoServiceConfig.verbose = verbose
        KAutoServiceConfig.messageCollector = messageCollector

        FirExtensionRegistrarAdapter.registerExtension(KAutoServiceFirRegistrar())
        IrGenerationExtension.registerExtension(KAutoServiceIrExtension(verbose, messageCollector, configuration))
    }
}
