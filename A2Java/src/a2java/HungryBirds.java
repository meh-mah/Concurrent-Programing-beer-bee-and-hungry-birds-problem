/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a2java;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mehran
 */
public class HungryBirds
{
    
    public static int WORMBUF = 5;
    public static int CHILDS = 15;
    
    // semaphors used to signal no worm condition
    
    public static Semaphore empty = new Semaphore(0); /* sem empty = 1 */
    
    /*  In Semaphore, no guarantee that the first thread that call acquire() is also the first thread to obtain a permit.
     * e.g  we have 2 baby bird and only one worm. first baby eats the worm, second one waits to obtain the permit,
     * at this time first one back to eat and it also waits to acquire the permit.
     * Java Semaphore may schedule to give the permit to first one first. If this happen the second baby may never acquire a permit.
     * To enforce fairness, the Semaphore constructor with a Boolean true is used.
     * so the first thread waiting to obtain a permit will be granted the permit first (FIFO).
     * Therefore, in our example, the first baby has to wait to eat after the second one.
     * In this way second baby will have the same chance of eating as the first one.
     */
    //the worm plate is initialy full
    public static Semaphore full = new Semaphore(1, true); /* sem full = 0 */
    
    public static void main(String[] args)
    {
        
        Parent parent = new Parent();
        parent.start();
        
        for ( int i=0; i<CHILDS; i++ )
        {
            Child child = new Child();
            child.start();
        }
    }
}

class Parent extends Thread {
    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                // wait until no worm is left and empty semaphore signalled by child
                HungryBirds.empty.acquire();
                
                /*critical section. Protected by empty semaphor.
                 * Child processes can not enter their CS to chenge WORMBUF since the semaphor full is zero when parent process is in this section.
                 * Also there is only one parent process, so there is not any other thread who may want to increment WORMBUF
                 */
                HungryBirds.WORMBUF=+ 5;
                
                System.out.println("Parent huntedv " + HungryBirds.WORMBUF + " worms.");

                HungryBirds.full.release();
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(Parent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

class Child extends Thread
{
    private String name=getName();
    Random rand = new Random(); 
    
    
    @Override
    public void run()
    {
        int sleeptime;
        while (true)
        {
            try {
                // Acquires a permit to eat a worm if there is any or, blocking until one is available.
                HungryBirds.full.acquire();
                
                //child eats a worm
                //access to the shared buffer so requires syncronization
                //since acquire method is synchronized and value of full semaphor is one, only one thread can enter CS

                if (HungryBirds.WORMBUF>0){
                    HungryBirds.WORMBUF--;
                    System.out.println(name + " ate a worm.");
                    HungryBirds.full.release();
                    //HungryBirds.mutex.release();
                    //time to nap
                    sleeptime=rand.nextInt(7000)+2000;
                    System.out.println(name + " slept for 5 sec");
                    Thread.sleep(5000);
                }
                //there is no worm so call the parent 
                else if(HungryBirds.WORMBUF == 0){
                    System.out.println(name + " is hungry and no worm");
                    HungryBirds.empty.release();
                }
                
               
            } catch (InterruptedException ex) {
                Logger.getLogger(Child.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
} 
