package com.angorasix.events

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * Application context test.
 *
 * @author rozagerardo
 */
@SpringBootTest(
    properties = ["spring.data.mongodb.uri=mongodb://" + "\${embedded.mongodb.host}:\${embedded.mongodb.port}/" + "\${embedded.mongodb.database}"],
)
class EventsApplicationTest {

    @Test
    fun contextLoads() {
        // empty block since we only want to test that the Context loads correctly
    }
}
