package com.example.strayanimal.adoption;

import com.example.strayanimal.user.User;
import com.example.strayanimal.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdoptionFollowupService {

    private final AdoptionFollowupRepository followupRepository;
    private final AdoptionApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public AdoptionFollowupService(AdoptionFollowupRepository followupRepository,
                                   AdoptionApplicationRepository applicationRepository,
                                   UserRepository userRepository) {
        this.followupRepository = followupRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    public AdoptionFollowup addFollowup(Long applicationId, Long staffId, AdoptionFollowup req) {
        AdoptionApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("领养申请不存在"));
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("工作人员不存在"));

        AdoptionFollowup f = new AdoptionFollowup();
        f.setApplication(application);
        f.setStaff(staff);
        f.setVisitTime(req.getVisitTime());
        f.setVisitType(req.getVisitType());
        f.setVisitResult(req.getVisitResult());
        f.setNote(req.getNote());
        return followupRepository.save(f);
    }

    public Page<AdoptionFollowup> listByApplication(Long applicationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return followupRepository.findByApplication_Id(applicationId, pageable);
    }
}

