package com.layby.domain.repository;

import com.layby.domain.entity.CertificationEntity;
import com.layby.domain.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificationRepository extends JpaRepository<CertificationEntity, Long> {
}
