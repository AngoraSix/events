package com.angorasix.events

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

/**
 * Spring Boot main class for Events.
 *
 * @author rozagerardo
 */
@SpringBootApplication
@ConfigurationPropertiesScan("com.angorasix.events.infrastructure.config.configurationproperty.api")
class EventsApplication

/**
 * Main application method.
 *
 * @param args java args
 */
fun main(args: Array<String>) {
    runApplication<EventsApplication>(args = args)
}
