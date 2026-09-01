package com.codecriticon.claimscore.claim.application.usecase;

import java.util.List;
import java.util.UUID;

import com.codecriticon.claimscore.claim.application.port.in.query.UserClaimQuery;
import com.codecriticon.claimscore.claim.application.port.out.database.ClaimRepository;
import com.codecriticon.claimscore.claim.domain.model.Claim;

import org.springframework.stereotype.Service;

@Service
public class DefaultUserClaimQuery implements UserClaimQuery {

    private final ClaimRepository repository;

    public DefaultUserClaimQuery(ClaimRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Claim> findByPolicyId(UUID policyId) {
        return repository.findByPolicyId(policyId);
    }
}
