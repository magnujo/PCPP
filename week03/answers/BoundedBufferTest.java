import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

class BoundedBuffer<T> {

    private LinkedList<T> bq = new LinkedList<T>();
    private ConcurrentLinkedQueue<T> bqConc = new ConcurrentLinkedQueue<T>();
    private final Object lock = new Object();
    private final int max;

    BoundedBuffer(int max){
        this.max = max;
    }

    public void insert (T elem) throws InterruptedException {
        synchronized (lock){
            while(bq.size() == max){
                System.out.println("Waiting for free space...");
                lock.wait();
            }
            System.out.print(Thread.currentThread() + "is adding: " + elem);
            bq.add(elem);
            System.out.println("; Queue size: " + bq.size());
            lock.notifyAll();
        }
    }

    public T take() throws InterruptedException {
       synchronized (lock){
           while(bq.size() == 0){
               System.out.println("waiting for tasks...");
               lock.wait();
           }
           //final T remove = bqConc.removeFirst();
           final T remove = bq.removeFirst();
           System.out.print(Thread.currentThread() + "is taking: " + remove);
           System.out.println("; Queue size: " + bq.size());
           lock.notifyAll();
           return remove;
       }
    }
}

public class BoundedBufferTest{
    public static void main (String[] args){
        final int in = 1000;
        final int out = 5000;
        final BoundedBuffer<Integer> q = new BoundedBuffer<Integer>(10);
        final Random ran = new Random();
        final Thread t1 = new Thread(() -> {
            for (int i = 0; i < in; i++) {
                try {
                    q.insert(ran.nextInt(1000));
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    System.out.println("Thread 1 was interrupted");
                }
            }
        });

        final Thread t2 = new Thread(() -> {
            for (int i = 0; i < out; i++) {
                try {
                    final int elem = q.take();
                    Thread.sleep(elem);
                } catch (InterruptedException e) {
                    System.out.println("Thread 2 was interrupted");
                }
            }
        });

        t1.start();
        t2.start();
        try { t1.join(); t2.join(); }
        catch (final InterruptedException exn) {
            System.out.println("Some thread was interrupted");
        }







    }
}