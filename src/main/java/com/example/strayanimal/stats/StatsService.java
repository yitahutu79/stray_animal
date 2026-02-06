package com.example.strayanimal.stats;

import com.example.strayanimal.adoption.AdoptionApplicationRepository;
import com.example.strayanimal.animal.AnimalRepository;
import com.example.strayanimal.donation.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StatsService {

    private final AnimalRepository animalRepository;
    private final AdoptionApplicationRepository adoptionApplicationRepository;
    private final DonationRepository donationRepository;

    public StatsService(AnimalRepository animalRepository,
                        AdoptionApplicationRepository adoptionApplicationRepository,
                        DonationRepository donationRepository) {
        this.animalRepository = animalRepository;
        this.adoptionApplicationRepository = adoptionApplicationRepository;
        this.donationRepository = donationRepository;
    }

    public DashboardStats overview() {
        long totalAnimals = animalRepository.count();
        long animalsWaiting = animalRepository.countByStatus("待领养");
        long animalsAdopted = animalRepository.countByStatus("已领养");

        long totalApplications = adoptionApplicationRepository.count();
        long approvedApplications = adoptionApplicationRepository.countByStatus("APPROVED");

        long totalDonations = donationRepository.count();
        Double totalMoney = donationRepository.sumAllMoney();

        return new DashboardStats(
                totalAnimals,
                animalsWaiting,
                animalsAdopted,
                totalApplications,
                approvedApplications,
                totalDonations,
                totalMoney != null ? totalMoney : 0.0
        );
    }
}

