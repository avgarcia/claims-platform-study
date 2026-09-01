package com.codecriticon.claimscore.claim.infrastructure.database.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.codecriticon.claimscore.claim.application.port.out.database.ClaimRepository;
import com.codecriticon.claimscore.claim.domain.model.Claim;

import org.springframework.stereotype.Repository;

/**
 * In-memory implementation of the {@code ClaimRepository} interface.
 *
 * This class provides functionality for managing {@code Claim} entities using an internal
 * {@code HashMap} as the storage mechanism. It supports operations to retrieve claims
 * associated with a specific policy ID and to persist new claims into the repository.
 *
 * This implementation is suitable for testing, prototyping, or scenarios where persistence
 * to an actual database is not required.
 *
 * Thread Safety:
 * This class is not thread-safe since it relies on a shared {@code HashMap} without synchronization.
 * If used in a multi-threaded environment, external synchronization mechanisms must be employed.
 *
 * Responsibilities:
 * - Retrieve claims based on policy ID.
 * - Persist claims into in-memory storage.
 *
 * Limitations:
 * - Does not provide persistent storage; all data is stored in memory.
 *   Consequently, all stored claims are lost when the application shuts down.
 * - Is not suitable for concurrent use without additional synchronization.
 */
@Repository
public class GenericClaimRepository implements ClaimRepository {

    private final Map<UUID, Claim> claims = new HashMap<>();

    /**
     * Retrieves a list of claims associated with the specified policy ID.
     *
     * This method filters through the repository's in-memory storage to find all claims
     * that are linked to the given policy ID. It returns all matching claims in a list,
     * or an empty list if no matches are found.
     *
     * @param policyId The unique identifier of the policy whose related claims need to be fetched.
     *                 Must not be {@code null}.
     * @return A list of {@code Claim} instances associated with the given policy ID.
     *         Returns an empty list if no claims are found for the specified policy.
     */
    @Override
    public List<Claim> findByPolicyId(UUID policyId) {
        return claims.values().stream()
            .filter(claim -> claim.policyId().equals(policyId))
            .toList();
    }

    /**
     * Persists a {@code Claim} instance into the repository.
     *
     * This method stores the claim in the underlying in-memory map, associating it with its unique
     * identifier as the key. It overwrites any existing entry with the same identifier. The stored
     * claim may later be retrieved using methods such as {@code findByPolicyId}.
     *
     * @param claim The {@code Claim} instance to be saved. Must not be {@code null}.
     *              It should contain all necessary details, including a unique identifier and
     *              other associated metadata.
     * @throws IllegalArgumentException If the {@code claim} is {@code null}.
     */
    @Override
    public void save(Claim claim) {
        claims.put(claim.id(), claim);
    }
}
