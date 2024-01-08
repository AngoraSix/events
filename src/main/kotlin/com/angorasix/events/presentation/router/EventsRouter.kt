package com.angorasix.events.presentation.router

import com.angorasix.commons.reactive.presentation.filter.extractRequestingContributor
import com.angorasix.commons.reactive.presentation.filter.extractResourceAdmins
import com.angorasix.events.infrastructure.config.configurationproperty.api.ApiConfigs
import com.angorasix.events.presentation.handler.EventsHandler
import org.springframework.web.reactive.function.server.CoRouterFunctionDsl
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.coRouter

/**
 * Router for all Project related endpoints.
 *
 * @author rozagerardo
 */
class EventsRouter(
    private val handler: EventsHandler,
    private val apiConfigs: ApiConfigs,
) {

    /**
     * Main RouterFunction configuration for all endpoints related to Projects.
     *
     * @return the [RouterFunction] with all the routes for Projects
     */
    fun eventRouterFunction() = coRouter {
        filter { request, next ->
            extractRequestingContributor(request, next)
        }
        apiConfigs.basePaths.events.nest {
            apiConfigs.routes.baseAdministeredResourceEventRoute.nest {
                defineClubMemberAddedEndpoint()
            }
        }
    }

    private fun CoRouterFunctionDsl.defineClubMemberAddedEndpoint() {
        path(apiConfigs.routes.administeredResourceEvent.path).nest {
            filter { request, next ->
                extractResourceAdmins(request, next, true)
            }
            method(
                apiConfigs.routes.administeredResourceEvent.method,
                handler::administeredResourceEvent,
            )
        }
    }
}
