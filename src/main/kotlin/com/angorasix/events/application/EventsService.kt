package com.angorasix.events.application

import com.angorasix.commons.domain.SimpleContributor
import com.angorasix.commons.infrastructure.intercommunication.A6DomainResource
import com.angorasix.commons.infrastructure.intercommunication.messaging.dto.A6InfraMessageDto
import com.angorasix.events.domain.events.Event
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.messaging.support.MessageBuilder

/**
 * Service providing functionality for Events.
 *
 * @author rozagerardo
 */
class EventsService(private val streamBridge: StreamBridge) {

    /**
     * Method to handle administred resource event, publishing it to the corresponding message bind.
     */
    fun processA6ResourceEvent(
        event: Event,
        affectedContributorsIds: List<String>,
        objectId: String,
        requestingContributor: SimpleContributor,
    ): Boolean =
        affectedContributorsIds.map {
            streamBridge.send(
                "events",
                MessageBuilder.withPayload(
                    A6InfraMessageDto(
                        it,
                        A6DomainResource.CONTRIBUTOR,
                        objectId,
                        event.subjectType,
                        event.subjectEvent,
                        requestingContributor,
                        event.eventData,
                    ),
                ).build(),
            )
        }.all { it }
}
