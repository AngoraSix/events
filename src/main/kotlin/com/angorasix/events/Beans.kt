package com.angorasix.events

import com.angorasix.events.application.EventsService
import com.angorasix.events.infrastructure.security.EventsSecurityConfiguration
import com.angorasix.events.presentation.handler.EventsHandler
import com.angorasix.events.presentation.router.EventsRouter
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

val beans = beans {
    bean {
        EventsSecurityConfiguration().springSecurityFilterChain(ref())
    }
    bean<EventsService>()
    bean<EventsHandler>()
    bean {
        EventsRouter(ref(), ref()).eventRouterFunction()
    }
}

class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(context: GenericApplicationContext) = beans.initialize(context)
}
