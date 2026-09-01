package com.codecriticon.claimscore.claim.application.port.out.database;

import java.util.List;
import java.util.UUID;

import com.codecriticon.claimscore.claim.domain.model.Claim;

/**
 * A repository interface for managing {@code Claim} entities.
 *
 * This interface defines methods for retrieving and persisting insurance claims. It serves as an
 * abstraction for any data access logic related to claims, allowing implementations to interact
 * with various underlying storage mechanisms such as databases, in-memory structures, or external
 * systems.
 *
 * Responsibilities:
 * - Retrieve claims associated with a specific insurance policy.
 * - Save new claims to the repository for processing or storage.
 *
 * Implementations should ensure thread safety if used in multi-threaded environments and honor
 * validation rules to prevent null parameters being passed.
 */
public interface ClaimRepository {

    /**
     * Retrieves a list of insurance claims associated with a specific policy ID.
     *
     * This method queries the underlying data storage to fetch all claims linked
     * to the given policy identifier. If no claims are found for the specified
     * policy ID, an empty list is returned.
     *
     * @param policyId The unique identifier of the insurance policy whose claims are
     *                 to be retrieved. Must not be {@code null}.
     * @return A list of {@code Claim} instances linked to the specified policy ID.
     *         Returns an empty list if no claims are associated with the policy ID.
     * @throws IllegalArgumentException If {@code policyId} is {@code null}.
     */
    List<Claim> findByPolicyId(UUID policyId);

    /**
     * Persists the provided {@code Claim} entity to the repository.
     *
     * This method is used to save a claim instance, ensuring its persistence in
     * the underlying storage mechanism. It is expected that the {@code Claim} parameter
     * contains all necessary and valid data before invocation.
     *
     * @param claim The {@code Claim} instance to be saved. Must not be {@code null}.
     * @throws IllegalArgumentException If the provided {@code claim} is {@code null}.
     */
    void save(Claim claim);

}
