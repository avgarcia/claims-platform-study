package com.codecriticon.claimscore.claim.domain.model;

/**
 * Represents the potential statuses of an insurance claim within the domain model.
 *
 * A claim can have one of the following states:
 * - {@code SUBMITTED}: Indicates the claim has been initially submitted and is awaiting further review.
 * - {@code APPROVED}: Indicates the claim has been reviewed and approved.
 * - {@code REJECTED}: Indicates the claim has been reviewed and rejected.
 *
 * These statuses are used throughout the domain to track the life cycle of an insurance claim
 * and validate transitions between states.
 */
public enum ClaimStatus {
    SUBMITTED,
    APPROVED,
    REJECTED
}
