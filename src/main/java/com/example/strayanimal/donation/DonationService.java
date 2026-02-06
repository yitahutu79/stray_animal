package com.example.strayanimal.donation;

import com.example.strayanimal.user.User;
import com.example.strayanimal.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DonationService {

    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final DonationUsageRepository donationUsageRepository;

    public DonationService(DonationRepository donationRepository,
                           UserRepository userRepository,
                           DonationUsageRepository donationUsageRepository) {
        this.donationRepository = donationRepository;
        this.userRepository = userRepository;
        this.donationUsageRepository = donationUsageRepository;
    }

    public Donation createDonation(Long donorId, Donation req) {
        Donation donation = new Donation();
        if (donorId != null) {
            User donor = userRepository.findById(donorId)
                    .orElseThrow(() -> new IllegalArgumentException("捐赠人不存在"));
            donation.setDonor(donor);
            donation.setDonorName(donor.getRealName() != null ? donor.getRealName() : donor.getUsername());
        } else {
            donation.setDonorName(req.getDonorName());
        }
        donation.setType(req.getType());
        donation.setAmount(req.getAmount());
        donation.setPublicFlag(req.getPublicFlag() != null ? req.getPublicFlag() : Boolean.TRUE);
        donation.setRemark(req.getRemark());
        donation.setStatus("PENDING");
        return donationRepository.save(donation);
    }

    public Page<Donation> listPublic(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return donationRepository.findByPublicFlagTrue(pageable);
    }

    public Page<Donation> listAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return donationRepository.findAll(pageable);
    }

    public Donation updateStatus(Long id, String status) {
        Donation d = donationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("捐赠记录不存在"));
        d.setStatus(status);
        return donationRepository.save(d);
    }

    public DonationUsage addUsage(Long donationId, DonationUsage req) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new IllegalArgumentException("捐赠记录不存在"));
        DonationUsage usage = new DonationUsage();
        usage.setDonation(donation);
        usage.setAnimal(req.getAnimal());
        usage.setProjectName(req.getProjectName());
        usage.setUseTime(req.getUseTime());
        usage.setUseDesc(req.getUseDesc());
        usage.setPhotoUrl(req.getPhotoUrl());
        return donationUsageRepository.save(usage);
    }

    public Page<DonationUsage> listUsageByDonation(Long donationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return donationUsageRepository.findByDonation_Id(donationId, pageable);
    }
}

