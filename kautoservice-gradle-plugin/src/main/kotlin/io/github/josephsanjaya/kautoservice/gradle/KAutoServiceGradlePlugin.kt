package io.github.josephsanjaya.kautoservice.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class KAutoServiceGradlePlugin : KotlinCompilerPluginSupportPlugin {

    override fun apply(target: Project) {
        target.extensions.create("kautoservice", KAutoServiceExtension::class.java)
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    override fun getCompilerPluginId(): String = "io.github.josephsanjaya.kautoservice"

    override fun getPluginArtifact(): SubpluginArtifact =
        SubpluginArtifact("io.github.josephsanjaya.kautoservice", "kautoservice-compiler-plugin", "1.0.0")

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project
        val extension = project.extensions.getByType(KAutoServiceExtension::class.java)

        // Inject the compiler plugin and annotation dependencies
        project.dependencies.add("kotlinCompilerPluginClasspath", "io.github.josephsanjaya.kautoservice:kautoservice-compiler-plugin:1.0.0")
        project.dependencies.add(kotlinCompilation.implementationConfigurationName, "io.github.josephsanjaya.kautoservice:kautoservice-annotations:1.0.0")

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
