package com.codecriticon.claimscore.claim.application.usecase;

import com.codecriticon.claimscore.claim.application.port.out.database.ClaimRepository;
import com.codecriticon.claimscore.claim.domain.model.Claim;
import com.codecriticon.claimscore.claim.domain.model.ClaimStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DefaultSubmitClaimCommandTest {

    private final Clock clock = Clock.fixed(Instant.parse("2026-08-30T10:00:00Z"), ZoneOffset.UTC);

    @Mock
    private ClaimRepository repository;

    private DefaultSubmitClaimCommand command;

    @BeforeEach
    void setUp() {
        command = new DefaultSubmitClaimCommand(clock, repository);
    }

    @Test
    @DisplayName("Should submit claim successfully with valid policy ID and description")
    void shouldSubmitClaimSuccessfully() {
        UUID policyId = UUID.randomUUID();
        String description = "Description";

        Claim claim = command.submit(policyId, description);

        assertNotNull(claim);
        assertEquals(policyId, claim.policyId());
        assertEquals(description, claim.description());
        assertEquals(ClaimStatus.SUBMITTED, claim.status());

        ArgumentCaptor<Claim> captor = ArgumentCaptor.forClass(Claim.class);
        verify(repository).save(captor.capture());
        assertEquals(claim, captor.getValue());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when policy ID is null")
    void shouldThrowExceptionWhenPolicyIdIsNull() {
        assertThrows(NullPointerException.class, () -> command.submit(null, "Description"));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when description is blank")
    void shouldThrowExceptionWhenDescriptionIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> command.submit(UUID.randomUUID(), ""));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when description is only whitespaces")
    void shouldThrowExceptionWhenDescriptionIsWhitespace() {
        assertThrows(
            IllegalArgumentException.class,
            () -> command.submit(UUID.randomUUID(), "   ")
        );
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when description is null")
    void shouldThrowExceptionWhenDescriptionIsNull() {
        assertThrows(NullPointerException.class, () -> command.submit(UUID.randomUUID(), null));
    }

    @Test
    @DisplayName("Should generate claim with non-null ID")
    void shouldGenerateClaimWithNonNullId() {
        UUID policyId = UUID.randomUUID();
        String description = "Description";

        Claim claim = command.submit(policyId, description);

        assertNotNull(claim.id());
    }

    @Test
    @DisplayName("Should set submittedAt timestamp when claim is submitted")
    void shouldSetSubmittedAtTimestamp() {
        UUID policyId = UUID.randomUUID();
        String description = "Description";

        Claim claim = command.submit(policyId, description);

        assertNotNull(claim.submittedAt());
    }

    @Test
    @DisplayName("Should use fixed clock timestamp for submittedAt")
    void shouldUseFixedClockTimestamp() {
        UUID policyId = UUID.randomUUID();
        String description = "Description";

        Claim claim = command.submit(policyId, description);

        assertEquals(
            Instant.parse("2026-08-30T10:00:00Z").atZone(ZoneOffset.UTC).toLocalDateTime(),
            claim.submittedAt()
        );
    }

    @Test
    @DisplayName("Should generate unique IDs for different claims")
    void shouldGenerateUniqueIds() {
        UUID policyId = UUID.randomUUID();
        String description = "Description";

        Claim claim1 = command.submit(policyId, description);
        Claim claim2 = command.submit(policyId, description);

        assertNotEquals(claim1.id(), claim2.id());
    }

    @Test
    @DisplayName("Should call repository save exactly once")
    void shouldCallRepositorySaveOnce() {
        UUID policyId = UUID.randomUUID();
        String description = "Description";

        command.submit(policyId, description);

        verify(repository).save(org.mockito.ArgumentMatchers.any(Claim.class));
    }

    @Test
    @DisplayName("Should accept description with leading and trailing spaces")
    void shouldAcceptDescriptionWithSpaces() {
        UUID policyId = UUID.randomUUID();
        String description = "  Valid Description  ";

        Claim claim = command.submit(policyId, description);

        assertNotNull(claim);
        assertEquals(description, claim.description());
    }
}
