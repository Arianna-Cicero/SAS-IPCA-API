package com.ipca

import io.ktor.server.testing.*
import kotlin.test.BeforeTest

abstract class IntegrationTest {

    protected fun testApp(block: suspend ApplicationTestBuilder.() -> Unit) {
        testApplication {
            application {
                module()
            }
            block()
        }
    }
}
