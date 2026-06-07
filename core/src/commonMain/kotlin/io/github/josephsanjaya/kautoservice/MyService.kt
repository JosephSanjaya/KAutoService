package io.github.josephsanjaya.kautoservice

import io.github.josephsanjaya.kautoservice.annotations.AutoService

interface MyService {
    fun hello(): String
}

@AutoService(MyService::class)
class MyServiceImpl : MyService {
    override fun hello(): String = "Hello from MyServiceImpl"
}

