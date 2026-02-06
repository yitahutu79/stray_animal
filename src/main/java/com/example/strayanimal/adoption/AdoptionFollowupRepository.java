package com.example.strayanimal.adoption;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionFollowupRepository extends JpaRepository<AdoptionFollowup, Long> {

    Page<AdoptionFollowup> findByApplication_Id(Long applicationId, Pageable pageable);
}

