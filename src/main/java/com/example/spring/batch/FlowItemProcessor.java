package com.example.spring.batch;

import java.util.random.RandomGenerator;

import org.springframework.batch.item.ItemProcessor;

public class FlowItemProcessor implements ItemProcessor<String, JobConfiguration.Flow> {

   @Override
   public JobConfiguration.Flow process( final String item ) throws Exception {
      final int timeout = RandomGenerator.getDefault().nextInt( 2000, 5000 );
      System.out.println( "FlowItemProcessor " + item + " sleeps for " + timeout );
      Thread.sleep( timeout );
      return JobConfiguration.Flow.valueOf( item );
   }
}