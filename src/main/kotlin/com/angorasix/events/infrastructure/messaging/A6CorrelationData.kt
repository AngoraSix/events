package com.angorasix.events.infrastructure.messaging

import com.angorasix.commons.infrastructure.intercommunication.messaging.A6InfraMessageDto
import org.springframework.amqp.rabbit.connection.CorrelationData

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
class A6CorrelationData(id: String, val payload: A6InfraMessageDto) : CorrelationData(id)