package com.example.strayanimal.donation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationUsageRepository extends JpaRepository<DonationUsage, Long> {

    Page<DonationUsage> findByDonation_Id(Long donationId, Pageable pageable);
}
