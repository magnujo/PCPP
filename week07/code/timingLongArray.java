CountDownLatch everybodyReady = new CountDownLatch(NO_READERS+NO_ADDERS);
CountDownLatch startSignal = new CountDownLatch(1);
CountDownLatch allDone = new CountDownLatch(NO_READERS+NO_ADDERS);

Thread[] readers = new Thread[NO_READERS];
long[] dummies = new long[NO_READERS];
for (int i = 0; i<NO_READERS;i++){
  final int index=i;
  Thread t = new Thread( () -> {
    everybodyReady.countDown();
    try {startSignal.await();} catch (Exception ex){};
    for (int j = 0; j<100_000; j++) dummies[index] += dal1.get(2);
    allDone.countDown();
  });
  readers[i] = t;
  t.start();
}
Thread[] adders = new Thread[NO_ADDERS];
for (int i = 0; i<NO_ADDERS;i++){
  final int index=i;
  Thread t = new Thread( () -> {
    everybodyReady.countDown();
    try {startSignal.await();} catch (Exception ex){};
    for (int j = 0; j<100_000; j++) dal1.add(117);
    allDone.countDown();
  });
  adders[i] = t;
  t.start();
}

System.out.println("Waiting for all to start");
//everybodyReady.await();

System.out.println("Starting all");
long start = System.nanoTime();
startSignal.countDown();
//allDone.await();
long total = System.nanoTime()-start;

System.out.printf("Total time: %,d\n", total);