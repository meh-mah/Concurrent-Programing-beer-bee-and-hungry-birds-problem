*Short description of the applications:
The program is implementing the hungry birds problem using the semaphore and java multithread. Each baby brides considered as a thread and the parent is a separate thread. This achieved as Child and Parent classes extends java Thread.
The parent is responsible to fill the worm dish (which is a shared variable) by 5 worms and notifies Children by using V operation of semaphore. Since children processes are waiting on P(full) operation of semaphore upon receiving the full semaphore signal start to eat one worm each and goes to sleep. And the one who sees the plate empty signal to the parent that dish is empty (V(empty)), so, parent start to fill the dish with 5 worms again. As incrementing and decrementing the shared variable worm dish is critical section, the semaphore used in a way that to ensure mutual exclusive access to the dish. 
In Semaphore, no guarantee that the first thread that call acquire() is also the first thread to obtain a permit. e.g  we have 2 baby bird and only one worm. first baby eats the worm, second one waits to obtain the permit, at this time first one back to eat and it also waits to acquire the permit. Java Semaphore may schedule to give the permit to first one first. If this happen the second baby may never acquire a permit To enforce fairness, the Semaphore constructor with a Boolean true is used. so the first thread waiting to obtain a permit will be granted the permit first (FIFO). Therefore, in our example, the first baby has to wait to eat after the second one. In this way second baby will have the same chance of eating as the first one.

*Description of command-line parameters: 
There is no command line parameter. The numbers of baby bird are 15 by default, and the number of worm in the dish is initially 5 and increment by 5 worms whenever the dish is empty.

*Instructions to build and to run the application:
Javac HungryBirds.java     // to compile
Java HungryBirds //to run
