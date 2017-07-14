/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a2java;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mehran
 */
public class BearAndBee {
    //number of bees
    public final int BEES_QTY=12;
    //Pot Max capacity
    public final int HONEY_PORTIONS_MAX=3;
    //Current amount of honey in pot
    public int CURRENT_PORTION=0;
    
    //semaphores used to signal when the pot is full or still not full
    
     /*In Semaphore, no guarantee that the first thread that call acquire() is also the first thread to obtain a permit. e.g  we have 2 bees and the pot capacity is 1.
      * first bee fills the pot, second one should wait to obtain the permit( until the honey eaten by the bear),
      * at this time first one also back to acquire permit to fill the pot.
      * Java Semaphore may schedule to give the permit to first one first. If this happen the second bee may never acquire a permit.
      * Therefore most of the work load would be on first bee (thread), which is not fair.
      * i.e.  poor load balancing. To enforce fairness, the Semaphore constructor with a Boolean true is used.
      * So the first thread waiting to obtain a permit will be granted the permit first (FIFO).
      * Therefore, in our example, the first bee has to wait until the second one filled the pot.
      * In this way bees will have fair work load balance.
      */
    //the pot is initialy empty
    public Semaphore not_full=new Semaphore(1, true);
    public Semaphore full=new Semaphore(0);
    public BearAndBee() {

        // initiate bees threads and bear thread
        for (int i = 0; i < BEES_QTY; i++) {
            Bee bee = new Bee();
            bee.start();
        }
        Bear bear = new Bear();
        bear.start();
    }
    
    private class Bee extends Thread {
        private String name=getName();
        @Override
        public void run() {
            while (true) {
                try {
                    //wait until pot is not full
                    not_full.acquire();
                    //fill the pot by one portion
                    CURRENT_PORTION++;
                    System.out.println("Bee ("+name+") Filled the pot. Current portion= "+CURRENT_PORTION);
                    //If the pot is full, signal full semaphore to awakwen the bear. if still not full, signal not full semaphore to other bee.
                    if (CURRENT_PORTION == HONEY_PORTIONS_MAX) {
                        full.release();
                        System.out.println("Pot is full, nothing to do let sleep");
                    } else {
                        not_full.release();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(BearAndBee.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private class Bear extends Thread {
        @Override
        public void run() {
            while(true) {
                try {
                    //wait until the pot becomes full
                    full.acquire();
                    //bear eats all honey
                    CURRENT_PORTION = 0;
                    System.out.println("Bear ate all honey");
                    //signal not full semaphore to bees, so they will start to fill the pot again
                    not_full.release();
                    //time to take a nap
                    sleep(6000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(BearAndBee.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public static void main(String[] args) {
        BearAndBee bearAndBee = new BearAndBee();

    } 
}
