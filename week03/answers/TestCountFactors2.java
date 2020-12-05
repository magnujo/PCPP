// For week 2
// sestoft@itu.dk * 2014-08-29

import java.util.concurrent.atomic.AtomicInteger;

class TestCountFactors2 {
  public static void main(String[] args) throws InterruptedException {
    final int inter = 500_000;
    //int count = 0;
    //final MyAtomicInteger count = new MyAtomicInteger(0);
    final AtomicInteger count = new AtomicInteger(0);

    Thread threads[] = new Thread[10];
    for (int i = 0; i < 10; i++) {
      final int start = i * inter;
      final int end = start + inter;
      threads[i] = new Thread(new Runnable() {
        @Override
        public void run() {
          for (int j = start; j < end; j++) {
            final int temp = countFactors(j);
            count.addAndGet(temp);
          }
        }
      });
    }

    long timestart = System.nanoTime();
    for (int i = 0; i < threads.length; i++) {
      threads[i].start();
    }
    for (int i = 0; i < threads.length; i++) {
      threads[i].join();
    }
    long timeend = System.nanoTime();
    long time = timeend-timestart;
    System.out.println("Time:" + time);
    System.out.printf("Total number of factors is %9d%n", count.get());

  }
  public static int countFactors(int p) {
    if (p < 2) 
      return 0;
    int factorCount = 1, k = 2;
    while (p >= k * k) {
      if (p % k == 0) {
	factorCount++;
	p /= k;
      } else 
	k++;
    }
    return factorCount;
  }
}
