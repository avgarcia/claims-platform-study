package com.codecriticon.claimscore.claim.application.usecase;

import com.codecriticon.claimscore.claim.application.port.out.database.ClaimRepository;
import com.codecriticon.claimscore.claim.domain.model.Claim;
import com.codecriticon.claimscore.claim.domain.model.ClaimStatus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserClaimQuery - Unit Tests")
class DefaultUserClaimQueryTest {

    @Mock
    private ClaimRepository repository;

    @InjectMocks
    private DefaultUserClaimQuery query;

    @Test
    @DisplayName("Should return claims when querying by policy ID")
    void shouldReturnClaimsByPolicyId() {
        UUID policyId = UUID.randomUUID();
        Claim claim = new Claim(UUID.randomUUID(), policyId, "Description", ClaimStatus.SUBMITTED, LocalDateTime.now());
        when(repository.findByPolicyId(policyId)).thenReturn(Collections.singletonList(claim));

        List<Claim> result = query.findByPolicyId(policyId);

        assertEquals(1, result.size());
        assertEquals(claim, result.get(0));
    }

    @Test
    @DisplayName("Should return empty list when no claims exist for policy ID")
    void shouldReturnEmptyListWhenNoClaimsExist() {
        UUID policyId = UUID.randomUUID();
        when(repository.findByPolicyId(policyId)).thenReturn(Collections.emptyList());

        List<Claim> result = query.findByPolicyId(policyId);

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should return multiple claims for a single policy ID")
    void shouldReturnMultipleClaimsForSinglePolicyId() {
        UUID policyId = UUID.randomUUID();
        Claim claim1 = new Claim(
            UUID.randomUUID(),
            policyId,
            "Description 1",
            ClaimStatus.SUBMITTED,
            LocalDateTime.now()
        );
        Claim claim2 = new Claim(
            UUID.randomUUID(),
            policyId,
            "Description 2",
            ClaimStatus.APPROVED,
            LocalDateTime.now()
        );
        Claim claim3 = new Claim(
            UUID.randomUUID(),
            policyId,
            "Description 3",
            ClaimStatus.REJECTED,
            LocalDateTime.now()
        );
        when(repository.findByPolicyId(policyId)).thenReturn(List.of(claim1, claim2, claim3));

        List<Claim> result = query.findByPolicyId(policyId);

        assertEquals(3, result.size());
        assertEquals(claim1, result.get(0));
        assertEquals(claim2, result.get(1));
        assertEquals(claim3, result.get(2));
    }
}
