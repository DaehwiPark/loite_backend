package com.boot.loiteMsBack.terms.repository;

import com.boot.loiteMsBack.terms.entity.TermsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsRepository extends JpaRepository<TermsEntity, Long> {
}
