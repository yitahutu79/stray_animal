package com.example.strayanimal.donation;

import com.example.strayanimal.animal.Animal;
import com.example.strayanimal.animal.AnimalRepository;
import com.example.strayanimal.user.User;
import com.example.strayanimal.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DonationService {

    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final DonationUsageRepository donationUsageRepository;
    private final AnimalRepository animalRepository;

    public DonationService(DonationRepository donationRepository,
                           UserRepository userRepository,
                           DonationUsageRepository donationUsageRepository,
                           AnimalRepository animalRepository) {
        this.donationRepository = donationRepository;
        this.userRepository = userRepository;
        this.donationUsageRepository = donationUsageRepository;
        this.animalRepository = animalRepository;
    }

    public Donation createDonation(Long donorId, Donation req) {
        Donation donation = new Donation();
        String requestedName = req.getDonorName() == null ? null : req.getDonorName().trim();
        boolean hasRequestedName = requestedName != null && !requestedName.isEmpty();
        if (donorId != null) {
            User donor = userRepository.findById(donorId)
                    .orElseThrow(() -> new IllegalArgumentException("捐赠人不存在"));
            donation.setDonor(donor);
            if (hasRequestedName) {
                donation.setDonorName(requestedName);
            } else {
                String fallbackName = donor.getRealName() != null && !donor.getRealName().trim().isEmpty()
                        ? donor.getRealName().trim()
                        : donor.getUsername();
                donation.setDonorName(fallbackName);
            }
        } else {
            donation.setDonorName(hasRequestedName ? requestedName : null);
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

    public Page<DonationSummaryDTO> listMyDonations(Long donorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createTime")));
        return donationRepository.findByDonor_IdOrderByCreateTimeDesc(donorId, pageable)
                .map(DonationSummaryDTO::fromEntity);
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
        if (req.getAnimal() != null && req.getAnimal().getId() != null) {
            Animal animal = animalRepository.findById(req.getAnimal().getId())
                    .orElseThrow(() -> new IllegalArgumentException("对应动物不存在"));
            usage.setAnimal(animal);
        }
        usage.setProjectName(req.getProjectName());
        usage.setUseTime(req.getUseTime());
        usage.setUseDesc(req.getUseDesc());
        usage.setPhotoUrl(req.getPhotoUrl());
        return donationUsageRepository.save(usage);
    }

    public Page<DonationUsageDTO> listUsageByDonation(Long donationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return donationUsageRepository.findByDonation_Id(donationId, pageable).map(DonationUsageDTO::fromEntity);
    }

    public Page<DonationUsageDTO> listRecentUsages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("useTime"), Sort.Order.desc("createTime")));
        return donationUsageRepository.findAll(pageable).map(DonationUsageDTO::fromEntity);
    }
}
