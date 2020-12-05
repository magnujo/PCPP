// For week 3
// sestoft@itu.dk * 2014-09-04
// thdy@itu.dk * 2019
// kasper@itu.dk * 2020

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

interface Histogram {
  public void increment(int bin);
  public int getCount(int bin);
  public float getPercentage(int bin);
  public int getSpan();
  public int getTotal();
  public int[] getBins();
}

public class SimpleHistogramMagnus {
    public static void main(String[] args) throws InterruptedException {
        final Histogram histogram = new Histogram4(30);
        final int inter = 500_000;

        Thread[] threads = new Thread[10];

        for (int i = 0; i < threads.length; i++) {
            final int start = i * inter;
            final int end = start + inter;
            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    final int temp = countFactors(j);
                    histogram.increment(temp);
                }
            });
        }
        final long starttime = System.nanoTime();
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }
        final long endtime = System.nanoTime();
        System.out.println(endtime-starttime);

        dump(histogram);

    }

    public static void dump(Histogram histogram) {
        for (int bin = 0; bin < histogram.getSpan(); bin++) {
            System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
        }
        System.out.printf("      %9d%n", histogram.getTotal() );
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

class Histogram4 implements Histogram{

    private final AtomicIntegerArray counts;
    private int total = 0;

    public Histogram4(int span) {
        this.counts = new AtomicIntegerArray(span);
    }

    @Override
    public void increment(int bin) {
        counts.incrementAndGet(bin);
        total++;
    }

    @Override
    public int getCount(int bin) {
        return counts.get(bin);
    }

    @Override
    public float getPercentage(int bin) {
        return getCount(bin) / getTotal() * 100;
    }

    @Override
    public int getSpan() {
        return counts.length();
    }

    @Override
    public int getTotal() {
        return total;
    }

    @Override
    public int[] getBins() {
        final int[] temp = new int[counts.length()];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = counts.get(i);
        }
        return temp;
    }
}


class Histogram3 implements Histogram{

    private final AtomicInteger[] counts;
    private int total = 0;

    public Histogram3(int span) {
        this.counts = new AtomicInteger[span];
        for (int i = 0; i < counts.length; i++) {
            counts[i] = new AtomicInteger(0);
        }
    }

    @Override
    public void increment(int bin) {
        counts[bin].incrementAndGet();
        total++;
    }

    @Override
    public int getCount(int bin) {
        return counts[bin].get();
    }

    @Override
    public float getPercentage(int bin) {
        return getCount(bin) / getTotal() * 100;
    }

    @Override
    public int getSpan() {
        return counts.length;
    }

    @Override
    public int getTotal() {
        return total;

    }

    @Override
    public int[] getBins() {
        final int[] temp = new int[counts.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = counts[i].get();
        }
        return temp;
    }
}



class Histogram2 implements Histogram{

    private final int[] counts;
    private int total = 0;
    private final Object totallock = new Object();

    public Histogram2(int span) {
        this.counts = new int[span];
    }

    @Override
    public synchronized void increment(int bin) {
        counts[bin] = counts[bin] + 1;
        total++;
    }

    @Override
    public synchronized int getCount(int bin) {
        return counts[bin];
    }

    @Override
    public synchronized float getPercentage(int bin) {
        return getCount(bin) / getTotal() * 100;
    }

    @Override
    public int getSpan() {
        return counts.length;
    }

    @Override
    public synchronized int getTotal() {
            return total;

    }

    @Override
    public synchronized int[] getBins() {
        return counts;
    }
}


class StripedHistogram2 implements Histogram{
    private final int[] counts;
    private AtomicInteger total = new AtomicInteger(0);
    private final Object totallock = new Object();
    private final Object[] binLocks;

    public StripedHistogram2(int span) {
        this.counts = new int[span];
        this.binLocks = new Object[span];
        for (int i = 0; i < binLocks.length; i++) {
            this.binLocks[i] = new Object();
        }
    }

    @Override
    public void increment(int bin) {
        synchronized (binLocks[bin]){
            counts[bin] = counts[bin] + 1;
        }
        total.incrementAndGet();

    }

    @Override
    public int getCount(int bin) {
        synchronized (binLocks[bin]){
            return counts[bin];
        }
    }

    @Override
    public float getPercentage(int bin) { return getCount(bin) / getTotal() * 100; }

    @Override
    public int getSpan() { return counts.length; }

    @Override
    public int getTotal() {
            return total.get();

    }

    @Override
    public int[] getBins() {
        return counts;
    }
}


class Histogram1 implements Histogram {
    private int[] counts;
    private int total=0;

    public Histogram1(int span) {
        this.counts = new int[span];
    }

    public void increment(int bin) {
        counts[bin] = counts[bin] + 1;
        total++;
    }

    public int getCount(int bin) {
        return counts[bin];
    }
    
    public float getPercentage(int bin){
      return getCount(bin) / getTotal() * 100;
    }

    public int getSpan() {
        return counts.length;
    }
    
    public int getTotal(){
      return total;
    }
}


