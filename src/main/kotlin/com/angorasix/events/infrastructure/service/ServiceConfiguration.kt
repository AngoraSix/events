package com.angorasix.events.infrastructure.service

import com.angorasix.events.application.EventsService
import com.angorasix.events.infrastructure.config.configurationproperty.api.ApiConfigs
import com.angorasix.events.presentation.handler.EventsHandler
import com.angorasix.events.presentation.router.EventsRouter
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfiguration {
    @Bean
    fun eventsService(streamBridge: StreamBridge) = EventsService(streamBridge)

    @Bean
    fun eventsHandler(service: EventsService) = EventsHandler(service)

    @Bean
    fun eventsRouter(
        handler: EventsHandler,
        apiConfigs: ApiConfigs,
    ) = EventsRouter(handler, apiConfigs).eventRouterFunction()
}
