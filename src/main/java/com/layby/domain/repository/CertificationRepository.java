package com.layby.domain.repository;

import com.layby.domain.entity.MailCertification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationRepository extends JpaRepository<MailCertification, Long> {

    MailCertification findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteAllByUsername(String username);
}
