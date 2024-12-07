package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("Create a subscribers")
    public ResponseEntity<Subscriber> createSubscriber(@Valid @RequestBody Subscriber sub) throws IdInvalidException {
        // check email exist
        if (this.subscriberService.isExistsByEmail(sub.getEmail()))
            throw new IdInvalidException("Subscriber với email = " + sub.getEmail() + " đã tồn tai");

        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.handleCreateSubscriber(sub));
    }

    @PutMapping("/subscribers")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody Subscriber subsReqeust) throws IdInvalidException {
        // check id
        Subscriber subsDB = this.subscriberService.fetchSubscriberById(subsReqeust.getId());
        if (subsDB == null) {
            throw new IdInvalidException("Subscriber với id = " + subsReqeust.getId() + " không tồn tại");
        }

        return ResponseEntity.ok(this.subscriberService.handleUpdateSubscriber(subsDB, subsReqeust));
    }

    @PostMapping("/subscribers/skills")
    @ApiMessage("Get subscriber's skill")
    public ResponseEntity<Subscriber> getSubscribersSkill() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        return ResponseEntity.ok().body(this.subscriberService.findByEmail(email));
    }
}
