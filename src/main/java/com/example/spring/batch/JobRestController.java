package com.example.spring.batch;

import java.time.Instant;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobRestController {

   @Autowired
   private JobLauncher jobLauncher;

   @Autowired
   private Job job;

   @GetMapping
   public ResponseEntity<String> startJob()
         throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
      jobLauncher.run( job, createJobParameters() );
      return ResponseEntity.ok( "Start job" );
   }

   public static JobParameters createJobParameters() {
      return new JobParametersBuilder()
            .addLong( "lastModifiedAt", Instant.now().toEpochMilli() )
            .toJobParameters();
   }
}
