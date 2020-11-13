import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

class IncrementSafelyTest {
  public static void main(String[] args) throws Exception{
    final int N = 5;
    for(int i=0; i < N; i++) testIncrementer( new IncrementerBasic() );
    for(int i=0; i < N; i++) testIncrementer( new IncrementerSync() );
    for(int i=0; i < N; i++) testIncrementer( new IncrementerAtomic() );
    for(int i=0; i < N; i++) testIncrementer( new IncrementerCASWrong() );
    for(int i=0; i < N; i++) testIncrementer( new IncrementerCASRight() );
  }
  
  static void testIncrementer(Incrementer incr) throws Exception {
    final int noCores = 12;
    final int noThreads = noCores * 10;
    final int noReps = 10_000;
    final CyclicBarrier barrier = new CyclicBarrier(noThreads+1);
    Runnable incrBody = () -> {
      try {
        barrier.await(); // wait for all to be ready
        //if ( start.get() == 0) start.set( System.nanoTime() );
        for(int i=0; i < noReps; i++) incr.increment();
        barrier.await(); // release main thread
      } catch (Exception ie) {}
    };
    for (int i=0; i < noThreads; i++)
      new Thread( incrBody ).start();
    
    barrier.await(); // release all threads
    long start = System.nanoTime();
    barrier.await(); // wait for them all to finish
    
    long time = System.nanoTime()-start;
    String res = incr.get() == noThreads*noReps ? "SUCCESS" : "FAIL";
    System.out.print(incr.getClass().getName() + " test " + res);
    System.out.printf(" in %,dµs \n", time/1_000);
  }
}

interface Incrementer {
  void increment();
  int get();
}

class IncrementerBasic implements Incrementer{
  int counter = 0;
  public void increment(){ counter++; }
  public int get(){ return counter; }
}

class IncrementerSync implements Incrementer{
  int counter = 0;
  public synchronized void increment(){ counter++; }
  public int get(){ return counter; }
}

class IncrementerAtomic implements Incrementer{
  AtomicInteger counter = new AtomicInteger(0);
  public void increment(){ counter.incrementAndGet(); }
  public int get(){ return counter.get(); }
}

class IncrementerCASWrong implements Incrementer{
  AtomicInteger counter = new AtomicInteger(0);
  public void increment(){ 
    int current = counter.get();
    counter.compareAndSet(current, current+1);
  }
  public int get(){ return counter.get(); }
}

class IncrementerCASRight implements Incrementer{
  AtomicInteger counter = new AtomicInteger(0);
  public void increment(){ 
    int current = counter.get();
    while( !counter.compareAndSet(current, current+1) )
      current = counter.get();
  }
  public int get(){ return counter.get(); }
}