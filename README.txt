*Short description of the applications:
The program is implementing the bear and bee problem using the semaphore and java multithread. Each bee considered as a thread and bear is a separate thread. This achieved as Bees and Bear classes extends java Thread.
The bees are responsible to fill the pot (which is a shared variable) by one portion and last bee who fulls the pot notifies bear by using V operation of semaphore. Since bear process is waiting on P(full) operation of semaphore upon receiving the full semaphore signal eats all honey and signal to the bees that pot id empty (V(empty)), so, bees start to fill the pot again. As incrementing and decrementing the shared variable pot is critical section, the semaphore used in a way that to ensure mutual exclusive access to the pot. 
 In Semaphore, no guarantee that the first thread that call acquire() is also the first thread to obtain a permit. e.g  we have 2 bees and the pot capacity is 1. first bee fills the pot, second one should wait to obtain the permit( until the honey eaten by the bear), at this time first one also back to acquire permit to fill the pot. Java Semaphore may schedule to give the permit to first one first. If this happen the second bee may never acquire a permit. Therefore most of the work load would be on first bee (thread), which is not fair. i.e.  poor load balancing. To enforce fairness, the Semaphore constructor with a Boolean true is used. so the first thread waiting to obtain a permit will be granted the permit first (FIFO). Therefore, in our example, the first bee has to wait until the second one filled the pot. In this way bees will have fair work load balance.

*Description of command-line parameters: 
There is no command line parameter. The numbers of bees are 12 by default, and the maximum capacity of the pot is three.

*Instructions to build and to run the application:
Javac BearAndBee.java     // to compile
Java BearAndBee //to run
