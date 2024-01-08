package com.angorasix.events.presentation.handler

import com.angorasix.commons.domain.SimpleContributor
import com.angorasix.commons.infrastructure.constants.AngoraSixInfrastructure
import com.angorasix.commons.infrastructure.intercommunication.events.A6InfraEventDto
import com.angorasix.commons.reactive.presentation.error.resolveBadRequest
import com.angorasix.events.application.EventsService
import com.angorasix.events.domain.events.Event
import com.angorasix.events.infrastructure.config.configurationproperty.api.ApiConfigs
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.accepted
import org.springframework.web.reactive.function.server.ServerResponse.status
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.buildAndAwait

/**
 * Project Handler (Controller) containing all handler functions related to Project endpoints.
 *
 * @author rozagerardo
 */
class EventsHandler(
    private val eventsService: EventsService,
    private val apiConfigs: ApiConfigs,
) {

    /**
     * Handler for the List Projects endpoint, retrieving a Flux including all persisted Projects.
     *
     * @param request - HTTP `ServerRequest` object
     * @return the `ServerResponse`
     */
    suspend fun administeredResourceEvent(request: ServerRequest): ServerResponse {
        val requestingContributor =
            request.attributes()[AngoraSixInfrastructure.REQUEST_ATTRIBUTE_CONTRIBUTOR_KEY]
        return if (requestingContributor is SimpleContributor) {
            val event = try {
                request.awaitBody<A6InfraEventDto>()
                    .convertToDomain(requestingContributor)
            } catch (e: IllegalArgumentException) {
                return resolveBadRequest(
                    e.message ?: "Incorrect Event body",
                    "Event",
                )
            }
            val admins =
                request.attributes()[AngoraSixInfrastructure.REQUEST_ATTRIBUTE_RESOURCE_ADMINS_KEY]
            return if (admins is List<*>) {
                return if (eventsService.processAdministeredResourceEvent(
                        event,
                        admins.filterIsInstance<String>(),
                        event.subjectId,
                    )
                ) {
                    return accepted().buildAndAwait()
                } else {
                    status(HttpStatus.SERVICE_UNAVAILABLE).buildAndAwait()
                }
            } else {
                resolveBadRequest("Incorrect Administered Resource Event body", "Event")
            }
        } else {
            resolveBadRequest("Invalid Contributor Token", "Contributor Token")
        }
    }

    private fun A6InfraEventDto.convertToDomain(requestingContributor: SimpleContributor): Event =
        Event(subjectType, subjectId, subjectEvent, eventData, requestingContributor)

}