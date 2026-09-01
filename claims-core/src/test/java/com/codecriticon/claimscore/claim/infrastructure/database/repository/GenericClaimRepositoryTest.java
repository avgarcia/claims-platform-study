package com.codecriticon.claimscore.claim.infrastructure.database.repository;

import com.codecriticon.claimscore.claim.domain.model.Claim;
import com.codecriticon.claimscore.claim.domain.model.ClaimStatus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GenericClaimRepositoryTest {

    private final GenericClaimRepository repository = new GenericClaimRepository();

    private Claim createClaim(UUID id, UUID policyId, String description, ClaimStatus status) {
        return new Claim(id, policyId, description, status, LocalDateTime.now());
    }

    @Test
    @DisplayName("Debería guardar y recuperar una reclamación")
    void shouldSaveAndRetrieveClaim() {
        UUID policyId = UUID.randomUUID();
        Claim claim = createClaim(
            UUID.randomUUID(),
            policyId,
            "Description",
            ClaimStatus.SUBMITTED
        );

        repository.save(claim);
        List<Claim> claims = repository.findByPolicyId(policyId);

        assertNotNull(claims);
        assertEquals(1, claims.size());
        assertEquals(claim, claims.get(0));
    }

    @Test
    @DisplayName("Debería cambiar el estado de enviado a aprobado")
    void shouldChangeStatusFromSubmittedToApproved() {
        Claim claim = createClaim(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "Description",
            ClaimStatus.SUBMITTED
        );

        Claim updatedClaim = claim.changeStatus(ClaimStatus.APPROVED);

        assertEquals(ClaimStatus.APPROVED, updatedClaim.status());
    }

    @Test
    @DisplayName("Debería cambiar el estado de enviado a rechazado")
    void shouldChangeStatusFromSubmittedToRejected() {
        Claim claim = createClaim(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "Description",
            ClaimStatus.SUBMITTED
        );

        Claim updatedClaim = claim.changeStatus(ClaimStatus.REJECTED);

        assertEquals(ClaimStatus.REJECTED, updatedClaim.status());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando la transición de estado es inválida")
    void shouldThrowExceptionWhenInvalidStatusTransition() {
        Claim approvedClaim = createClaim(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "Description",
            ClaimStatus.APPROVED
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> approvedClaim.changeStatus(ClaimStatus.REJECTED)
        );
    }

    @Test
    @DisplayName("Debería guardar y recuperar múltiples reclamaciones de la misma póliza")
    void shouldSaveAndRetrieveMultipleClaimsForSamePolicy() {
        UUID policyId = UUID.randomUUID();
        Claim claim1 = createClaim(
            UUID.randomUUID(),
            policyId,
            "First claim",
            ClaimStatus.SUBMITTED
        );
        Claim claim2 = createClaim(
            UUID.randomUUID(),
            policyId,
            "Second claim",
            ClaimStatus.APPROVED
        );

        repository.save(claim1);
        repository.save(claim2);
        List<Claim> claims = repository.findByPolicyId(policyId);

        assertNotNull(claims);
        assertEquals(2, claims.size());
        assertTrue(claims.contains(claim1));
        assertTrue(claims.contains(claim2));
    }

    @Test
    @DisplayName("Debería almacenar reclamaciones de diferentes pólizas por separado")
    void shouldStoreDifferentPoliciesClaimsSeparately() {
        UUID policyId1 = UUID.randomUUID();
        UUID policyId2 = UUID.randomUUID();
        Claim claim1 = createClaim(
            UUID.randomUUID(),
            policyId1,
            "Policy 1 claim",
            ClaimStatus.SUBMITTED
        );
        Claim claim2 = createClaim(
            UUID.randomUUID(),
            policyId2,
            "Policy 2 claim",
            ClaimStatus.SUBMITTED
        );

        repository.save(claim1);
        repository.save(claim2);
        List<Claim> claims1 = repository.findByPolicyId(policyId1);
        List<Claim> claims2 = repository.findByPolicyId(policyId2);

        assertEquals(1, claims1.size());
        assertEquals(claim1, claims1.get(0));
        assertEquals(1, claims2.size());
        assertEquals(claim2, claims2.get(0));
    }

    @Test
    @DisplayName("Debería devolver lista vacía cuando no existen reclamaciones para una póliza")
    void shouldReturnEmptyListWhenNoClaimsExistForPolicy() {
        UUID nonExistentPolicyId = UUID.randomUUID();

        List<Claim> claims = repository.findByPolicyId(nonExistentPolicyId);

        assertNotNull(claims);
        assertTrue(claims.isEmpty());
    }

    @Test
    @DisplayName("Debería lanzar excepción al cambiar de rechazado a aprobado")
    void shouldThrowExceptionWhenChangingFromRejectedToApproved() {
        Claim rejectedClaim = createClaim(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "Description",
            ClaimStatus.REJECTED
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> rejectedClaim.changeStatus(ClaimStatus.APPROVED)
        );
    }

    @Test
    @DisplayName("Debería lanzar excepción al cambiar de aprobado a enviado")
    void shouldThrowExceptionWhenChangingFromApprovedToSubmitted() {
        Claim approvedClaim = createClaim(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "Description",
            ClaimStatus.APPROVED
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> approvedClaim.changeStatus(ClaimStatus.SUBMITTED)
        );
    }

    @Test
    @DisplayName("Debería lanzar excepción al cambiar de rechazado a enviado")
    void shouldThrowExceptionWhenChangingFromRejectedToSubmitted() {
        Claim rejectedClaim = createClaim(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "Description",
            ClaimStatus.REJECTED
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> rejectedClaim.changeStatus(ClaimStatus.SUBMITTED)
        );
    }

    @Test
    @DisplayName("Debería mantener la inmutabilidad al cambiar el estado")
    void shouldMaintainImmutabilityWhenChangingStatus() {
        Claim originalClaim = createClaim(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "Description",
            ClaimStatus.SUBMITTED
        );

        Claim updatedClaim = originalClaim.changeStatus(ClaimStatus.APPROVED);

        assertEquals(ClaimStatus.SUBMITTED, originalClaim.status());
        assertEquals(ClaimStatus.APPROVED, updatedClaim.status());
        assertEquals(originalClaim.id(), updatedClaim.id());
        assertEquals(originalClaim.policyId(), updatedClaim.policyId());
        assertEquals(originalClaim.description(), updatedClaim.description());
        assertEquals(originalClaim.submittedAt(), updatedClaim.submittedAt());
    }
}
