package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    public boolean isExistsByEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public Subscriber handleCreateSubscriber(Subscriber subs) {
        // check skills
        if (subs.getSkills() != null) {
            List<Long> ids = subs.getSkills().stream().map(item -> item.getId()).collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(ids);

            subs.setSkills(dbSkills);
        }

        return this.subscriberRepository.save(subs);
    }

    public Subscriber fetchSubscriberById(long id) {
        Optional<Subscriber> subOptional = this.subscriberRepository.findById(id);
        if (subOptional.isPresent())
            return subOptional.get();
        return null;
    }

    public Subscriber handleUpdateSubscriber(Subscriber subsDB, Subscriber subsReqeust) {
        // check skills
        if (subsReqeust.getSkills() != null) {
            List<Long> ids = subsReqeust.getSkills().stream().map(item -> item.getId()).collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(ids);

            subsDB.setSkills(dbSkills);
        }

        return this.subscriberRepository.save(subsDB);
    }
}
