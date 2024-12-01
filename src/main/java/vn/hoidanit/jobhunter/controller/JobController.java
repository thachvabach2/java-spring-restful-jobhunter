package vn.hoidanit.jobhunter.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    public final JobService jobService;
    public final CompanyService companyService;
    public final SkillService skillService;

    public JobController(JobService jobService, CompanyService companyService, SkillService skillService) {
        this.jobService = jobService;
        this.companyService = companyService;
        this.skillService = skillService;
    }

    @PostMapping("/job")
    @ApiMessage("create a job")
    public ResponseEntity<ResCreateJobDTO> createJob(@Valid @RequestBody Job job) throws IdInvalidException {

        // check null fields
        if (job.getCompany() == null || job.getSkills() == null) {
            throw new IdInvalidException("Phải có Fields Company và Skill");
        }

        ResCreateJobDTO res = this.jobService.handleCreateJob(job);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/job")
    @ApiMessage("update a job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job) throws IdInvalidException {
        Optional<Job> jobOptional = this.jobService.fetchJobById(job.getId());
        if (!jobOptional.isPresent()) {
            throw new IdInvalidException("Job not found");
        }

        return ResponseEntity.ok(this.jobService.handleUpdateJob(job));
    }

    @DeleteMapping("/job/{id}")
    @ApiMessage("delete a job by id")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> jobOptional = this.jobService.fetchJobById(id);
        if (!jobOptional.isPresent()) {
            throw new IdInvalidException("Job not found");
        }
        this.jobService.handleDeleteJob(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/job")
    @ApiMessage("fetch all jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.jobService.handleFetchAllJobs(spec, pageable));
    }

    @GetMapping("/job/{id}")
    @ApiMessage("fetch job by id")
    public ResponseEntity<Job> getJobById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> jobOptional = this.jobService.fetchJobById(id);
        if (!jobOptional.isPresent()) {
            throw new IdInvalidException("Job not found");
        }
        return ResponseEntity.ok(jobOptional.get());
    }
}
