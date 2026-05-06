package com.incidentops.auth.service;

import com.incidentops.auth.domain.OtpChallenge;
import com.incidentops.auth.domain.OtpFlowType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpChallengeRepository extends JpaRepository<OtpChallenge, Long> {

    Optional<OtpChallenge> findTopByFlowTypeAndEmailAndConsumedAtIsNullOrderByCreatedAtDesc(
            OtpFlowType flowType,
            String email
    );

    Optional<OtpChallenge> findTopByFlowTypeAndUsernameAndConsumedAtIsNullOrderByCreatedAtDesc(
            OtpFlowType flowType,
            String username
    );

    long deleteByFlowTypeAndEmail(OtpFlowType flowType, String email);

    long deleteByFlowTypeAndUsername(OtpFlowType flowType, String username);
}
