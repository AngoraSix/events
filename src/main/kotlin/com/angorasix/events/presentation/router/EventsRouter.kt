package com.angorasix.events.presentation.router

import com.angorasix.commons.reactive.presentation.filter.extractAffectedContributors
import com.angorasix.commons.reactive.presentation.filter.extractRequestingContributor
import com.angorasix.events.infrastructure.config.configurationproperty.api.ApiConfigs
import com.angorasix.events.presentation.handler.EventsHandler
import org.springframework.web.reactive.function.server.CoRouterFunctionDsl
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.coRouter

/**
 * Router for all Events related endpoints.
 *
 * @author rozagerardo
 */
class EventsRouter(
    private val handler: EventsHandler,
    private val apiConfigs: ApiConfigs,
) {
    /**
     * Main RouterFunction configuration for all endpoints related to Events.
     *
     * @return the [RouterFunction] with all the routes for Events
     */
    fun eventRouterFunction() =
        coRouter {
            filter { request, next ->
                extractRequestingContributor(request, next)
            }
            apiConfigs.basePaths.events.nest {
                apiConfigs.routes.baseA6ResourceEventRoute.nest {
                    defineA6ResourceEventEndpoint()
                }
            }
        }

    private fun CoRouterFunctionDsl.defineA6ResourceEventEndpoint() {
        path(apiConfigs.routes.a6ResourceEvent.path).nest {
            filter { request, next ->
                extractAffectedContributors(request, next, true)
            }
            method(
                apiConfigs.routes.a6ResourceEvent.method,
                handler::a6ResourceEvent,
            )
        }
    }
}
