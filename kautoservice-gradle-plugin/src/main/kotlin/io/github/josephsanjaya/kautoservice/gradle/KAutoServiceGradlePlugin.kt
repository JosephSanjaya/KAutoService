package io.github.josephsanjaya.kautoservice.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class KAutoServiceGradlePlugin : KotlinCompilerPluginSupportPlugin {

    companion object {
        // Fallback used when running from source (e.g., mavenLocal) without a MANIFEST.MF entry.
        const val PLUGIN_VERSION = "1.0.0"
    }


    override fun apply(target: Project) {
        target.extensions.create("kautoservice", KAutoServiceExtension::class.java)
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    override fun getCompilerPluginId(): String = "io.github.josephsanjaya.kautoservice"

    override fun getPluginArtifact(): SubpluginArtifact {
        val version = javaClass.`package`?.implementationVersion ?: PLUGIN_VERSION
        return SubpluginArtifact(
            "io.github.josephsanjaya.kautoservice",
            "kautoservice-compiler-plugin",
            version
        )
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project
        val extension = project.extensions.getByType(KAutoServiceExtension::class.java)
        val version = javaClass.`package`?.implementationVersion ?: PLUGIN_VERSION

        project.dependencies.add(
            "kotlinCompilerPluginClasspath",
            "io.github.josephsanjaya.kautoservice:kautoservice-compiler-plugin:$version"
        )
        project.dependencies.add(
            kotlinCompilation.defaultSourceSet.implementationConfigurationName,
            "io.github.josephsanjaya.kautoservice:kautoservice-annotations:$version"
        )

        return project.provider {
            listOf(
                SubpluginOption("enabled", extension.enabled.get().toString()),
                SubpluginOption("verify", extension.verify.get().toString()),
                SubpluginOption("verbose", extension.verbose.get().toString())
            )
        }
    }
}

open class KAutoServiceExtension(objects: org.gradle.api.model.ObjectFactory) {
    val enabled = objects.property(Boolean::class.java).convention(true)
    val verify = objects.property(Boolean::class.java).convention(true)
    val verbose = objects.property(Boolean::class.java).convention(false)
}
