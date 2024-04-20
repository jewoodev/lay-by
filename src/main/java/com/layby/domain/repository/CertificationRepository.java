package com.layby.domain.repository;

import com.layby.domain.entity.CertificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationRepository extends JpaRepository<CertificationEntity, Long> {
    CertificationEntity findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteAllByUsername(String username);
}
