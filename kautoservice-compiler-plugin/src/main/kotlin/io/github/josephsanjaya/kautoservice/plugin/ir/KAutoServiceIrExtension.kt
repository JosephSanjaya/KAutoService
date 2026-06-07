package io.github.josephsanjaya.kautoservice.plugin.ir

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.IrClassReference
import org.jetbrains.kotlin.ir.expressions.IrVararg
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.util.packageFqName
import org.jetbrains.kotlin.name.FqName
import java.io.File

class KAutoServiceIrExtension(
    private val verbose: Boolean,
    private val messageCollector: MessageCollector,
    private val configuration: CompilerConfiguration
) : IrGenerationExtension {

    private val autoServiceFqn = FqName("io.github.josephsanjaya.kautoservice.annotations.AutoService")

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val outputDir = configuration.get(JVMConfigurationKeys.OUTPUT_DIRECTORY)
        if (outputDir == null) {
            logVerbose("No output directory found. Skipping service files generation.")
            return
        }

        val servicesMap = mutableMapOf<String, MutableSet<String>>()
        val classes = mutableListOf<IrClass>()

        fun collectClasses(declarations: List<org.jetbrains.kotlin.ir.declarations.IrDeclaration>) {
            declarations.forEach { declaration ->
                if (declaration is IrClass) {
                    classes.add(declaration)
                    collectClasses(declaration.declarations)
                }
            }
        }

        moduleFragment.files.forEach { file ->
            collectClasses(file.declarations)
        }

        classes.forEach { processClass(it, servicesMap) }

        if (servicesMap.isEmpty()) return

        val servicesDir = File(outputDir, "META-INF/services")
        if (!servicesDir.exists()) {
            servicesDir.mkdirs()
        }

        servicesMap.forEach { (serviceInterface, implementers) ->
            val serviceFile = File(servicesDir, serviceInterface)
            logVerbose("Writing service file: ${serviceFile.absolutePath} with implementers: $implementers")
            serviceFile.bufferedWriter().use { writer ->
                implementers.sorted().forEach { implementer ->
                    writer.write(implementer)
                    writer.newLine()
                }
            }
        }
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    private fun processClass(irClass: IrClass, servicesMap: MutableMap<String, MutableSet<String>>) {
        val annotation = irClass.annotations.find {
            it.type.classFqName == autoServiceFqn
        } ?: return

        val implName = getJvmBinaryName(irClass)
        val vararg = annotation.arguments[0] as? IrVararg ?: return

        vararg.elements.forEach { element ->
            val classRef = element as? IrClassReference ?: return@forEach
            val referencedClass = classRef.symbol.owner as? IrClass ?: return@forEach
            val serviceName = getJvmBinaryName(referencedClass)
            servicesMap.getOrPut(serviceName) { mutableSetOf() }.add(implName)
        }
    }

    private fun getJvmBinaryName(irClass: IrClass): String {
        val parents = mutableListOf<String>()
        var current: IrDeclarationParent? = irClass
        while (current is IrClass) {
            parents.add(0, current.name.asString())
            current = current.parent
        }
        val pkg = irClass.packageFqName?.asString() ?: ""
        val classPart = parents.joinToString("$")
        return if (pkg.isEmpty()) classPart else "$pkg.$classPart"
    }

    private fun logVerbose(message: String) {
        if (verbose) {
            messageCollector.report(
                CompilerMessageSeverity.INFO,
                "[KAutoService] $message"
            )
        }
    }
}
