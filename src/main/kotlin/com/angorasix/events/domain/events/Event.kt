package com.angorasix.events.domain.events

import com.angorasix.commons.domain.A6Contributor

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
data class Event(
    val subjectType: String,
    val subjectId: String,
    val subjectEvent: String, // should match A6InfraTopics, not enforcing to avoid issues
    val eventData: Map<String, Any>,
    val requestingContributor: A6Contributor,
)
