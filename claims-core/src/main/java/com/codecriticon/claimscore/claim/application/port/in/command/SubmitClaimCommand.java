package com.codecriticon.claimscore.claim.application.port.in.command;

import java.util.UUID;

import com.codecriticon.claimscore.claim.domain.model.Claim;

/**
 * An interface defining the contract for submitting insurance claims.
 *
 * A claim represents a request made by a policyholder regarding a specific issue or incident
 * covered under an insurance policy. Implementations of this interface handle the creation and
 * submission of claims, ensuring proper validation, initialization, and persistence.
 *
 * Responsibilities:
 * - Validate the provided policy ID and claim description.
 * - Create a new {@code Claim} instance with relevant data.
 * - Submit and return the newly created claim.
 *
 * Methods:
 * - {@link #submit(UUID, String)}: Submits a new claim associated with a given insurance policy.
 */
public interface SubmitClaimCommand {

    /**
     * Submits a new claim associated with a specific insurance policy.
     *
     * This method validates the provided policy ID and description, creates a new {@code Claim}
     * instance, and submits the claim for processing. The resulting claim instance is returned
     * upon successful submission.
     *
     * @param policyId The unique identifier of the insurance policy associated with the claim.
     *                 Must not be {@code null}.
     * @param description A brief description or details about the claim.
     *                    Must not be {@code null} or empty.
     * @return A {@code Claim} instance representing the newly submitted claim.
     *         This includes details such as the claim's ID, description, status,
     *         and submission timestamp.
     * @throws IllegalArgumentException If {@code policyId} is {@code null}.
     * @throws IllegalArgumentException If {@code description} is {@code null} or blank.
     */
    Claim submit(UUID policyId, String description);

}
