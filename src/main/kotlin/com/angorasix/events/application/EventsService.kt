package com.angorasix.events.application

import com.angorasix.commons.infrastructure.intercommunication.A6DomainResource
import com.angorasix.commons.infrastructure.intercommunication.messaging.A6InfraMessageDto
import com.angorasix.events.domain.events.Event
import com.angorasix.events.infrastructure.messaging.A6CorrelationData
import org.springframework.amqp.core.Correlation
import org.springframework.amqp.rabbit.connection.CorrelationData
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.messaging.support.MessageBuilder
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


/**
 * Service providing functionality for Events.
 *
 * @author rozagerardo
 */
class EventsService(private val streamBridge: StreamBridge) {

    /**
     * Method to handle administred resource event, publishing it to the corresponding message bind.
     */
    fun processAdministeredResourceEvent(
        event: Event,
        admins: List<String>,
        objectId: String,
    ): Boolean =
        admins.map {
            val payload = A6InfraMessageDto(
                it,
                A6DomainResource.CONTRIBUTOR,
                objectId,
                event.subjectType,
                event.eventData,
            )
            val correlationData = A6CorrelationData(UUID.randomUUID().toString(), payload)
            streamBridge.send(
                "events",
                MessageBuilder.withPayload(payload)
                    .setHeader(AmqpHeaders.PUBLISH_CONFIRM_CORRELATION, correlationData)
                    .build(),
            )
            correlationData
        }.map {
            try {
                val confirm: CorrelationData.Confirm = it.future.get(10, TimeUnit.SECONDS)
                println("=====================")
                println("===================== CHEQUANDOO")
                println("=====================")
                println(confirm.toString() + " for " + it.payload)
                if (it.returned != null) {
                    println("=====================")
//                    log.error(("Message for " + correlation.getPayload()).toString() + " was returned ")
                    println("RERERERERERERERERETURNED ${it}")
                    println("=====================")
                    streamBridge.send(
                        "a6-dlx",
                        it.payload
                    )
                    return false
//                    "events.errorChannel"
                    // throw some exception to invoke binder retry/error handling
                }
            }
                catch (e: Exception) {
                    return false
                }
//            } catch (e: Exception) {
//                return false
//            }
            return true
        }.all { it }
}
