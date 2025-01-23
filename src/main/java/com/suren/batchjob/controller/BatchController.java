package com.suren.batchjob.controller;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchController {

    @Autowired
    public JobLauncher jobLauncher;

    @Autowired
    public Job job;

    @PostMapping("/invokejob")
    public String jobLauncher() {

        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();

        try {

            JobExecution execution = jobLauncher.run(job, jobParameters);

            return execution.getStatus().toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Batch job has been invoked";
        }



    }

}
