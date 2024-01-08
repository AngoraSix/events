package com.angorasix.events.infrastructure.messaging.error

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.support.ErrorMessage
import java.util.function.Consumer


/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
@Configuration // issues with Kotlin DSL for this
class MessagingErrorHandler {
    @Bean
    fun myErrorHandler(): Consumer<ErrorMessage> {
        return Consumer<ErrorMessage> { err -> println("GERGERGERGER ${err}") }
    }
}
