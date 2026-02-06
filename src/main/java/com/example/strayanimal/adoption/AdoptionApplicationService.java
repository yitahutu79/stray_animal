package com.example.strayanimal.adoption;

import com.example.strayanimal.animal.Animal;
import com.example.strayanimal.animal.AnimalRepository;
import com.example.strayanimal.user.User;
import com.example.strayanimal.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdoptionApplicationService {

    private final AdoptionApplicationRepository adoptionApplicationRepository;
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final AdoptionScoreCalculator scoreCalculator = new AdoptionScoreCalculator();

    public AdoptionApplicationService(AdoptionApplicationRepository adoptionApplicationRepository,
                                      UserRepository userRepository,
                                      AnimalRepository animalRepository) {
        this.adoptionApplicationRepository = adoptionApplicationRepository;
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
    }

    public AdoptionApplication apply(Long applicantId, Long animalId, AdoptionApplication request) {
        User applicant = userRepository.findById(applicantId)
                .orElseThrow(() -> new IllegalArgumentException("申请人不存在"));
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new IllegalArgumentException("动物不存在"));
        String animalStatus = animal.getStatus();
        if ("已领养".equals(animalStatus) || "特别照顾".equals(animalStatus)) {
            throw new IllegalStateException("该动物当前不可领养");
        }

        AdoptionApplication app = new AdoptionApplication();
        app.setApplicant(applicant);
        app.setAnimal(animal);
        app.setReason(request.getReason());
        app.setHomeEnv(request.getHomeEnv());
        app.setHasOtherPet(request.getHasOtherPet());
        app.setIncomeLevel(request.getIncomeLevel());
        app.setExperience(request.getExperience());
        app.setStatus("SUBMITTED");

        int score = scoreCalculator.calculate(app, animal);
        app.setScore(score);

        return adoptionApplicationRepository.save(app);
    }

    public Page<AdoptionApplicationDTO> listMyApplications(Long applicantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdoptionApplication> apps = adoptionApplicationRepository.findByApplicant_Id(applicantId, pageable);
        return apps.map(AdoptionApplicationDTO::fromEntity);
    }

    public Page<AdoptionAdminDTO> listByStatusForAdmin(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdoptionApplication> apps = adoptionApplicationRepository.findByStatusOrderByScoreDesc(status, pageable);
        return apps.map(AdoptionAdminDTO::fromEntity);
    }

    public AdoptionApplication updateStatus(Long id, String status, String rejectReason) {
        AdoptionApplication app = adoptionApplicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("申请不存在"));
        app.setStatus(status);
        app.setRejectReason(rejectReason);
        // 如果审核通过，则将动物状态更新为“已领养”，防止二次领养
        if ("APPROVED".equals(status)) {
            Animal animal = app.getAnimal();
            if (animal != null) {
                animal.setStatus("已领养");
                animalRepository.save(animal);
            }
        }
        return adoptionApplicationRepository.save(app);
    }

    /**
     * 按当前评分规则，重新计算所有历史申请的分数。
     */
    public void recalculateAllScores() {
        var all = adoptionApplicationRepository.findAll();
        for (AdoptionApplication app : all) {
            Animal animal = app.getAnimal();
            int score = scoreCalculator.calculate(app, animal);
            app.setScore(score);
        }
        adoptionApplicationRepository.saveAll(all);
    }
}

