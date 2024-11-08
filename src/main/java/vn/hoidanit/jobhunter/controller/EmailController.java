package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.service.EmailService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email")
    @ApiMessage("Send simple email")
    public String sendSimpleEmail() {
        // this.emailService.sendSimpleEmail();

        // this.emailService.sendEmailSync("ngocbach.crack@gmail.com", "test send
        // email", "<h1> <b> hello </b> </h1>",
        // false,
        // true);

        this.emailService.sendEmailFromTemplateSync("ngocbach.crack@gmail.com", "test send email", "job");
        return "ok";
    }

}
