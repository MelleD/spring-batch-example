package com.example.spring.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class FlowWriter implements ItemWriter<JobConfiguration.Flow> {

   @Override
   public void write( final List<? extends JobConfiguration.Flow> items ) throws Exception {
      System.out.println( "Flow Writer finish " + items.get( 0 ) );
   }
}