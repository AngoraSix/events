package com.angorasix.events.infrastructure.security

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers
import org.springframework.test.web.reactive.server.WebTestClient

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
@WebFluxTest // spins up only WebFlux + Security layers
@Import(SecurityConfiguration::class)
class SecurityConfigurationIntegrationTest {
    @Autowired
    lateinit var client: WebTestClient

    @Test
    fun `unauthenticated requests to protected resource get 401`() {
        client
            .get()
            .uri("/events")
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `authenticated requests with JWT are allowed`() {
        val jwt = makeTestJwt()
        client
            .mutateWith(SecurityMockServerConfigurers.mockJwt().jwt(jwt))
            .post()
            .uri("/events/a6-resource")
            .exchange()
            .expectStatus()
            .isNotFound // not found is enough to assure the request was authenticated
    }

    private fun makeTestJwt(): Jwt =
        Jwt
            .withTokenValue("test-token")
            .header("alg", "none")
            .claim("sub", "user123")
            .claim("scope", "read:data")
            .build()
}
