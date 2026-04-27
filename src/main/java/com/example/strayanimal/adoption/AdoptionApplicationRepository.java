package com.example.strayanimal.adoption;

import com.example.strayanimal.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionApplicationRepository extends JpaRepository<AdoptionApplication, Long> {

    Page<AdoptionApplication> findByApplicant(User applicant, Pageable pageable);

    // 通过 applicant_id 直接查询，避免必须先查 User 实体
    Page<AdoptionApplication> findByApplicant_Id(Long applicantId, Pageable pageable);

    Page<AdoptionApplication> findByStatusOrderByScoreDesc(String status, Pageable pageable);

    long countByStatus(String status);

    long countByAnimal_IdAndStatus(Long animalId, String status);
}

