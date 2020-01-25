package com.jahs.shared

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.jahs.Application
import com.jahs.context.order.modules.order.OrderFlow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("test")
@SpringBootTest(classes = arrayOf(Application::class),
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
open class ContextStarterTest {

    @LocalServerPort
    private val serverPort: Int = 0

    protected val objectMapper = ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
    protected val orderFlow = OrderFlow(objectMapper)

    @BeforeEach
    fun setUp() {
        RestAssuredTest.config(serverPort, objectMapper)
    }

}
