package com.example.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableBatchProcessing
@Configuration
public class JobConfiguration {

   public enum Flow {
      FLOW1,
      FLOW2,
      FLOW3,
      FLOW4
   }

   @Bean
   public ThreadPoolTaskExecutor jobTaskExecutor() {
      final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setCorePoolSize( 4 );
      executor.setMaxPoolSize( 25 );
      executor.setThreadNamePrefix( "jobTaskExecutor-" );
      executor.initialize();
      return executor;
   }

   @Bean
   public Job job( final ThreadPoolTaskExecutor jobTaskExecutor, final JobBuilderFactory jobBuilderFactory, final StepBuilderFactory stepFactory ) {

      final SimpleFlow flow1 = new FlowBuilder<SimpleFlow>( Flow.FLOW1.name() )
            .start( flow1( stepFactory ) )
            .end();
      final SimpleFlow flow2 = new FlowBuilder<SimpleFlow>( Flow.FLOW2.name() )
            .start( flow2( stepFactory ) )
            .end();
      final SimpleFlow flow3 = new FlowBuilder<SimpleFlow>( Flow.FLOW3.name() )
            .start( flow3( stepFactory ) )
            .end();
      final SimpleFlow flow4 = new FlowBuilder<SimpleFlow>( Flow.FLOW4.name() )
            .start( flow4( stepFactory ) )
            .end();

      return jobBuilderFactory.get( "JOB" ).preventRestart()
            .start( flow1 )
            .split( jobTaskExecutor )
            .add( flow2 )
            .split( jobTaskExecutor )
            .add( flow3 )
            .split( jobTaskExecutor )
            .add( flow4 )
            .end()
            .build();
   }

   @StepScope
   @Bean
   public FlowWriter writer() {
      return new FlowWriter();
   }

   @Bean
   public Step flow1( final StepBuilderFactory steps ) {
      return generateStep( steps, flow1Processor(), Flow.FLOW1 );
   }

   @Bean
   public Step flow2( final StepBuilderFactory steps ) {
      return generateStep( steps, flow2Processor(), Flow.FLOW2 );
   }

   @Bean
   public Step flow3( final StepBuilderFactory steps ) {
      return generateStep( steps, flow3Processor(), Flow.FLOW3 );
   }

   @Bean
   public Step flow4( final StepBuilderFactory steps ) {
      return generateStep( steps, flow4Processor(), Flow.FLOW4 );
   }

   @StepScope
   public ItemReader<String> itemReader( final Flow flow ) {
      return new FlowReader( flow );
   }

   private Step generateStep( final StepBuilderFactory steps, final ItemProcessor<String, Flow> itemProcessor, final Flow flow ) {
      return steps.get( flow.name() )
            .<String, Flow> chunk( 1 )
            .reader( itemReader( flow ) )
            .processor( itemProcessor )
            .writer( writer() )
            .build();
   }

   @StepScope
   @Bean
   public ItemProcessor<String, Flow> flow1Processor() {
      return new FlowItemProcessor();
   }

   @StepScope
   @Bean
   public ItemProcessor<String, Flow> flow2Processor() {
      return new FlowItemProcessor();
   }

   @StepScope
   @Bean
   public ItemProcessor<String, Flow> flow3Processor() {
      return new FlowItemProcessor();
   }

   @StepScope
   @Bean
   public ItemProcessor<String, Flow> flow4Processor() {
      return new FlowItemProcessor();
   }

}
