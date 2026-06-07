package io.github.josephsanjaya.kautoservice

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform