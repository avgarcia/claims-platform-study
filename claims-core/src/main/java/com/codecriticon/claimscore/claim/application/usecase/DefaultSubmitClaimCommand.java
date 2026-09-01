package com.codecriticon.claimscore.claim.application.usecase;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import com.codecriticon.claimscore.claim.application.port.in.command.SubmitClaimCommand;
import com.codecriticon.claimscore.claim.application.port.out.database.ClaimRepository;
import com.codecriticon.claimscore.claim.domain.model.Claim;
import com.codecriticon.claimscore.claim.domain.model.ClaimStatus;

import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * Implementation of the {@code SubmitClaimCommand} use case that handles the submission of insurance claims.
 *
 * This class validates and processes claims by associating them with a specified policy, assigning
 * the claim a unique identifier, setting its status to {@code SUBMITTED}, and recording the
 * submission timestamp. The processed claim is then saved to the repository for persistence.
 *
 * Responsibilities:
 * - Validate input parameters such as policy ID and claim description.
 * - Create a new {@code Claim} instance in the {@code SUBMITTED} status with relevant data.
 * - Persist the claim via the provided {@code ClaimRepository}.
 *
 * This class relies on:
 * - {@code ClaimRepository} for storing claims.
 *
 * Usage of this class ensures the consistency and integrity of submitted claims, enforcing
 * business rules around initial claim creation.
 */
@Service
public class DefaultSubmitClaimCommand implements SubmitClaimCommand {

    private final Clock clock;

    private final ClaimRepository repository;

    public DefaultSubmitClaimCommand(Clock clock, ClaimRepository repository) {
        this.clock = clock;
        this.repository = repository;
    }

    /**
     * Submits a new claim for a given policy and description.
     *
     * This method validates the provided policy ID and description, constructs a new claim
     * in the {@code SUBMITTED} status with the current timestamp, and persists it to the repository.
     *
     * @param policyId The unique identifier of the policy associated with the claim.
     *                 Must not be {@code null}.
     * @param description A brief explanation or details about the claim.
     *                    Must not be blank.
     * @return A {@code Claim} instance representing the submitted claim, including its unique ID,
     *         associated policy ID, description, status, and submission timestamp.
     * @throws IllegalArgumentException If the {@code policyId} is {@code null} or the {@code description}
     *                                  is blank.
     */
    @Override
    public Claim submit(UUID policyId, String description) {
        notNull(policyId, "El identificador de la póliza debe ser válido");
        notBlank(description, "La descripción no puede estar vacía");

        Claim submittedClaim = new Claim(
            UUID.randomUUID(),
            policyId,
            description,
            ClaimStatus.SUBMITTED,
            LocalDateTime.now(clock)
        );

        repository.save(submittedClaim);

        return submittedClaim;
    }
}
