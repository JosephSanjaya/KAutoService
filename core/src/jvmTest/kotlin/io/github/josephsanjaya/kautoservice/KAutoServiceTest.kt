package io.github.josephsanjaya.kautoservice

import java.util.ServiceLoader
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KAutoServiceTest {
    @Test
    fun testServiceLoading() {
        val services = ServiceLoader.load(MyService::class.java).toList()
        assertTrue(services.isNotEmpty(), "ServiceLoader should load at least one service")
        val service = services.firstOrNull()
        assertNotNull(service, "Service should not be null")
        assertTrue(service is MyServiceImpl, "Service should be MyServiceImpl")
    }
}
