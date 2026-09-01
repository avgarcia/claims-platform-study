package com.codecriticon.claimscore.claim.application.port.in.query;

import java.util.List;
import java.util.UUID;

import com.codecriticon.claimscore.claim.domain.model.Claim;

public interface UserClaimQuery {

    List<Claim> findByPolicyId(UUID policyId);

}
