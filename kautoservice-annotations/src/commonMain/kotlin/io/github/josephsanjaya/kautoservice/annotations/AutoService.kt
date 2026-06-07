package io.github.josephsanjaya.kautoservice.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class AutoService(vararg val value: KClass<*>)
