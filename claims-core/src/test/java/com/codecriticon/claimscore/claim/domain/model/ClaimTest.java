package com.codecriticon.claimscore.claim.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClaimTest {

    @Test
    @DisplayName("Debería crear una reclamación con datos válidos")
    void shouldCreateClaimWithValidData() {
        UUID id = UUID.randomUUID();
        UUID policyId = UUID.randomUUID();
        String description = "Test claim description";
        ClaimStatus status = ClaimStatus.SUBMITTED;
        LocalDateTime submittedAt = LocalDateTime.now();

        Claim claim = new Claim(id, policyId, description, status, submittedAt);

        assertNotNull(claim);
        assertEquals(id, claim.id());
        assertEquals(policyId, claim.policyId());
        assertEquals(description, claim.description());
        assertEquals(status, claim.status());
        assertEquals(submittedAt, claim.submittedAt());
    }

    @Test
    @DisplayName("Debería cambiar el estado de SUBMITTED a APPROVED")
    void shouldChangeStatusFromSubmittedToApproved() {
        Claim claim = createTestClaim(ClaimStatus.SUBMITTED);

        Claim updatedClaim = claim.changeStatus(ClaimStatus.APPROVED);

        assertStatusChangePreservesOtherFields(claim, updatedClaim, ClaimStatus.APPROVED);
    }

    @Test
    @DisplayName("Debería cambiar el estado de SUBMITTED a REJECTED")
    void shouldChangeStatusFromSubmittedToRejected() {
        Claim claim = createTestClaim(ClaimStatus.SUBMITTED);

        Claim updatedClaim = claim.changeStatus(ClaimStatus.REJECTED);

        assertStatusChangePreservesOtherFields(claim, updatedClaim, ClaimStatus.REJECTED);
    }

    @Test
    @DisplayName("Debería lanzar excepción al cambiar de SUBMITTED a SUBMITTED")
    void shouldThrowExceptionWhenChangingFromSubmittedToSubmitted() {
        Claim claim = createTestClaim(ClaimStatus.SUBMITTED);

        assertInvalidStatusChange(claim, ClaimStatus.SUBMITTED);
    }

    @Test
    @DisplayName("Debería lanzar excepción al cambiar de APPROVED a REJECTED")
    void shouldThrowExceptionWhenChangingFromApprovedToRejected() {
        Claim claim = createTestClaim(ClaimStatus.APPROVED);

        assertInvalidStatusChange(claim, ClaimStatus.REJECTED);
    }

    @Test
    @DisplayName("Debería lanzar excepción al cambiar de APPROVED a SUBMITTED")
    void shouldThrowExceptionWhenChangingFromApprovedToSubmitted() {
        Claim claim = createTestClaim(ClaimStatus.APPROVED);

        assertInvalidStatusChange(claim, ClaimStatus.SUBMITTED);
    }

    @Test
    @DisplayName("Debería lanzar excepción al cambiar de REJECTED a APPROVED")
    void shouldThrowExceptionWhenChangingFromRejectedToApproved() {
        Claim claim = createTestClaim(ClaimStatus.REJECTED);

        assertInvalidStatusChange(claim, ClaimStatus.APPROVED);
    }

    @Test
    @DisplayName("Debería lanzar excepción al cambiar de REJECTED a SUBMITTED")
    void shouldThrowExceptionWhenChangingFromRejectedToSubmitted() {
        Claim claim = createTestClaim(ClaimStatus.REJECTED);

        assertInvalidStatusChange(claim, ClaimStatus.SUBMITTED);
    }

    private Claim createTestClaim(ClaimStatus status) {
        return new Claim(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "Test claim description",
            status,
            LocalDateTime.now()
        );
    }

    private void assertStatusChangePreservesOtherFields(
        Claim original,
        Claim updated,
        ClaimStatus expectedStatus
    ) {
        assertEquals(expectedStatus, updated.status());
        assertEquals(original.id(), updated.id());
        assertEquals(original.policyId(), updated.policyId());
        assertEquals(original.description(), updated.description());
        assertEquals(original.submittedAt(), updated.submittedAt());
    }

    private void assertInvalidStatusChange(Claim claim, ClaimStatus newStatus) {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> claim.changeStatus(newStatus)
        );

        assertTrue(exception.getMessage().contains("No se puede pasar a"));
    }

}
