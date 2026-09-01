package com.codecriticon.claimscore.claim.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents an insurance claim in the domain model.
 * <p>
 * A claim is associated with an insurance policy and contains detailed information such as its
 * unique identifier, the policy it belongs to, a description provided during submission, its
 * current status, and the timestamp of when it was submitted.
 * <p>
 * Instances of this record are immutable, ensuring thread-safety and consistency within the
 * domain.
 * <p>
 * Fields: - id: The unique identifier of the claim. - policyId: The unique identifier of the
 * associated insurance policy. - description: A brief explanation or details regarding the claim. -
 * status: The current state of the claim (e.g., SUBMITTED, APPROVED, REJECTED). - submittedAt: The
 * timestamp indicating when the claim was submitted.
 */
public record Claim(
    UUID id,
    UUID policyId,
    String description,
    ClaimStatus status,
    LocalDateTime submittedAt
) {

    /**
     * Changes the current status of the claim to the specified new status, if the transition is valid.
     *
     * A status transition is considered valid if and only if:
     * - The current status is {@code SUBMITTED}, and
     * - The target status is either {@code APPROVED} or {@code REJECTED}.
     *
     * If the transition is invalid, this method throws an {@link IllegalArgumentException}.
     *
     * @param newStatus The new status to which the claim should transition. Must be either
     *                  {@code APPROVED} or {@code REJECTED} if the claim's current status is
     *                  {@code SUBMITTED}.
     * @return A new {@link Claim} instance representing the claim with the updated status. All
     *         other fields remain unchanged.
     * @throws IllegalArgumentException If the transition from the current status to the new status
     *                                  is not allowed.
     */
    public Claim changeStatus(ClaimStatus newStatus) {
        boolean isApprovedOrRejected =
            newStatus == ClaimStatus.APPROVED || newStatus == ClaimStatus.REJECTED;

        if (!(isApprovedOrRejected && status == ClaimStatus.SUBMITTED)) {
            throw new IllegalArgumentException(
                "No se puede pasar a %s cuando el estado actual es %s".formatted(newStatus, status)
            );
        }

        return new Claim(id, policyId, description, newStatus, submittedAt);
    }

}
