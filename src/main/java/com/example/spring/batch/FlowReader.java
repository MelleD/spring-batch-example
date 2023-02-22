package com.example.spring.batch;

import java.util.random.RandomGenerator;

import org.springframework.batch.item.ItemReader;

public class FlowReader implements ItemReader<String> {

   private boolean read;
   private final JobConfiguration.Flow flow;

   public FlowReader( final JobConfiguration.Flow flow ) {
      this.flow = flow;
   }

   @Override
   public String read() throws InterruptedException {
      final int timeout = RandomGenerator.getDefault().nextInt( 0, 5000 );
      System.out.println( "Flow Reader " + flow.name() + " sleeps for " + timeout );
      Thread.sleep( timeout );
      if ( read ) {
         // needs to complete the job
         return null;
      }

      return readFlow();
   }

   private String readFlow() {
      read = true;
      return flow.name();
   }
}
