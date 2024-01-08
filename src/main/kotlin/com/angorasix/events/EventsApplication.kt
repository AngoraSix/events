package com.angorasix.events

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.support.WebStack

/**
 * Spring Boot main class for Projects Core.
 *
 * @author rozagerardo
 */
@SpringBootApplication
@ConfigurationPropertiesScan("com.angorasix.events.infrastructure.config.configurationproperty.api")
class ProjectsCoreApplication

/**
 * Main application method.
 *
 * @param args java args
 */
fun main(args: Array<String>) {
    runApplication<ProjectsCoreApplication>(args = args)
}
