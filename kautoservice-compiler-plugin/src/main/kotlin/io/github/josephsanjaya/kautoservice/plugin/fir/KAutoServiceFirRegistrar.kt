package io.github.josephsanjaya.kautoservice.plugin.fir

import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class KAutoServiceFirRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::KAutoServiceClassChecker
    }
}
