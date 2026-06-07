package io.github.josephsanjaya.kautoservice.plugin.fir

import io.github.josephsanjaya.kautoservice.plugin.KAutoServiceConfig
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.DeclarationCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirClassChecker
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.fir.declarations.utils.isInner
import org.jetbrains.kotlin.fir.declarations.utils.modality
import org.jetbrains.kotlin.fir.expressions.FirAnnotation
import org.jetbrains.kotlin.fir.expressions.FirCollectionLiteral
import org.jetbrains.kotlin.fir.expressions.FirGetClassCall
import org.jetbrains.kotlin.fir.expressions.FirVarargArgumentsExpression
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.types.*
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity

class KAutoServiceClassChecker(session: FirSession) : FirAdditionalCheckersExtension(session) {
    override val declarationCheckers = object : DeclarationCheckers() {
        override val classCheckers: Set<FirClassChecker> = setOf(ClassChecker)
    }

    object ClassChecker : FirClassChecker(MppCheckerKind.Common) {
        private val AUTO_SERVICE_CLASS_ID = ClassId(
            FqName("io.github.josephsanjaya.kautoservice.annotations"),
            Name.identifier("AutoService")
        )

        @OptIn(SymbolInternals::class)
        context(context: CheckerContext, reporter: DiagnosticReporter)
        override fun check(declaration: FirClass) {
            val classSymbol = declaration.symbol
            val annotation = classSymbol.annotations.find {
                it.annotationTypeRef.coneType.classId == AUTO_SERVICE_CLASS_ID
            } ?: return

            val className = classSymbol.classId.asSingleFqName().asString()

            if (classSymbol.classKind != ClassKind.CLASS) {
                reportError("AutoService annotation can only be applied to classes, but was applied to $className")
                return
            }

            if (classSymbol.modality == Modality.ABSTRACT || classSymbol.modality == Modality.SEALED) {
                reportError("AutoService can only be applied to concrete classes, but $className is abstract or sealed")
                return
            }

            if (classSymbol.isInner) {
                reportError("AutoService cannot be applied to inner classes, but $className is inner")
                return
            }

            val serviceTypes = extractServiceTypes(annotation)
            if (serviceTypes.isEmpty()) {
                reportError("No service interfaces provided for AutoService on class $className")
                return
            }

            val classType = classSymbol.classId.constructClassLikeType(emptyArray(), false)
            for (serviceType in serviceTypes) {
                val isSubtype = classType.isSubtypeOf(serviceType, context.session)
                if (!isSubtype) {
                    val serviceName = serviceType.classId?.asSingleFqName()?.asString() ?: serviceType.toString()
                    reportError("Service provider $className does not implement declared service interface $serviceName")
                }
            }
        }

        private fun extractServiceTypes(annotation: FirAnnotation): List<ConeKotlinType> {
            val valueArg = annotation.argumentMapping.mapping[Name.identifier("value")] ?: return emptyList()
            val expressions = when (valueArg) {
                is FirVarargArgumentsExpression -> valueArg.arguments
                is FirCollectionLiteral -> valueArg.argumentList.arguments
                else -> listOf(valueArg)
            }
            return expressions.mapNotNull { expression ->
                val classId = extractClassIdFromExpression(expression) ?: return@mapNotNull null
                classId.constructClassLikeType(emptyArray(), false)
            }
        }

        private fun extractClassIdFromExpression(expression: org.jetbrains.kotlin.fir.expressions.FirExpression): ClassId? {
            return when (expression) {
                is FirGetClassCall -> extractClassIdFromExpression(expression.argument)
                is org.jetbrains.kotlin.fir.expressions.FirResolvedQualifier -> expression.classId
                else -> null
            }
        }

        private fun reportError(message: String) {
            KAutoServiceConfig.messageCollector.report(
                CompilerMessageSeverity.ERROR,
                "[KAutoService] $message"
            )
        }
    }
}
