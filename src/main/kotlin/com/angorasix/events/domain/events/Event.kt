package com.angorasix.events.domain.events

import com.angorasix.commons.domain.SimpleContributor
import com.angorasix.commons.infrastructure.intercommunication.A6DomainResource
import com.angorasix.commons.infrastructure.intercommunication.A6InfraTopics

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
data class Event (
    val subjectType: A6DomainResource,
    val subjectId: String,
    val subjectEvent: String, // should match A6InfraTopics, not enforcing to avoid issues
    val eventData: Map<String, Any>,
    val requestingContributor: SimpleContributor,
)