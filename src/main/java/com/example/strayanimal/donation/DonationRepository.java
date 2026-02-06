package com.example.strayanimal.donation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    Page<Donation> findByPublicFlagTrue(Pageable pageable);

    @Query("select coalesce(sum(d.amount),0) from Donation d where d.type = 'MONEY'")
    Double sumAllMoney();
}

