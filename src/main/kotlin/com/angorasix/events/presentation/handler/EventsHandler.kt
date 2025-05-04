package com.angorasix.events.presentation.handler

import com.angorasix.commons.domain.A6Contributor
import com.angorasix.commons.infrastructure.constants.AngoraSixInfrastructure
import com.angorasix.commons.infrastructure.intercommunication.events.GatewayEventTriggered
import com.angorasix.commons.reactive.presentation.error.resolveBadRequest
import com.angorasix.events.application.EventsService
import com.angorasix.events.domain.events.Event
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.accepted
import org.springframework.web.reactive.function.server.ServerResponse.status
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.buildAndAwait

/**
 * Events Handler (Controller) containing all handler functions related to Events endpoints.
 *
 * @author rozagerardo
 */
class EventsHandler(
    private val eventsService: EventsService,
) {
    /**
     * Handler to receive an event affecting an A6 Resource, retrieving Accepted response.
     *
     * @param request - HTTP `ServerRequest` object
     * @return the `ServerResponse`
     */
    suspend fun a6ResourceEvent(request: ServerRequest): ServerResponse {
        val requestingContributor =
            request.attributes()[AngoraSixInfrastructure.REQUEST_ATTRIBUTE_CONTRIBUTOR_KEY]
        return if (requestingContributor is A6Contributor) {
            val event =
                try {
                    request
                        .awaitBody<GatewayEventTriggered>()
                        .convertToDomain(requestingContributor)
                } catch (e: IllegalArgumentException) {
                    return resolveBadRequest(
                        e.message ?: "Incorrect Event body",
                        "Event",
                    )
                }
            val affectedContributors =
                request.attributes()[AngoraSixInfrastructure.REQUEST_ATTRIBUTE_AFFECTED_CONTRIBUTORS_KEY]
            if (affectedContributors is List<*>) {
                if (eventsService.processA6ResourceEvent(
                        event,
                        affectedContributors.filterIsInstance<String>(),
                        event.subjectId,
                        requestingContributor,
                    )
                ) {
                    accepted().buildAndAwait()
                } else {
                    status(HttpStatus.SERVICE_UNAVAILABLE).buildAndAwait()
                }
            } else {
                resolveBadRequest("Incorrect AngoraSix Resource Event body", "Event")
            }
        } else {
            resolveBadRequest("Invalid Contributor Token", "Contributor Token")
        }
    }

    private fun GatewayEventTriggered.convertToDomain(requestingContributor: A6Contributor): Event =
        Event(subjectType, subjectId, subjectEvent, eventData, requestingContributor)
}
