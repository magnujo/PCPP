import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.IntToDoubleFunction;


public class ExecutorAccountExperiments {

  static final int N = 10; // Number of accounts
  static final int NO_TRANSACTION=5;
  static int count = 0;

  static final Account[] accounts = new Account[N];
  static final Random rnd = new Random();
  
  public static void main(String[] args) throws InterruptedException {
    System.out.println(Runtime.getRuntime().availableProcessors());



    // Create empty accounts
    for( int i = 0; i < N; i++){
      accounts[i] = new Account(i);
    }

    Mark7("exectest", i -> {
      try {
        execTest();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    return i;
    });

    // doNTransactions(NO_TRANSACTION);

  }

  public static double Mark7(String msg, IntToDoubleFunction f) {
    int n = 10, count = 1, totalCount = 0;
    double dummy = 0.0, runningTime = 0.0, st = 0.0, sst = 0.0;
    do {
      count *= 2;
      st = sst = 0.0;
      for (int j=0; j<n; j++) {
        Timer t = new Timer();
        for (int i=0; i<count; i++)
          dummy += f.applyAsDouble(i);
        runningTime = t.check();
        double time = runningTime * 1e9 / count;
        st += time;
        sst += time * time;
        totalCount += count;
      }
    } while (runningTime < 0.25 && count < Integer.MAX_VALUE/2);
    double mean = st/n, sdev = Math.sqrt((sst - mean*mean*n)/(n-1));
    System.out.printf("%-25s %15.1f ns %10.2f %10d%n", msg, mean, sdev, count);
    return dummy / totalCount;
  }

  private static void execTest() throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    for (int i = 0; i < NO_TRANSACTION; i++) {
      executorService.submit(() -> doNTransactions(1));
    }

    executorService.shutdown();
    executorService.awaitTermination(1, TimeUnit.DAYS);
  }

  private static void doNTransactions(int noTransactions){
    for(int i = 0; i<noTransactions; i++){
      long amount = rnd.nextInt(5000)+100; // Just a random possitive amount
      int source = rnd.nextInt(N);
      int target = (source + rnd.nextInt(N-2)+1) % N; // make sure target <> source
      doTransaction( new Transaction( amount, accounts[source], accounts[target]));
    }
  }
  
  private static void doTransaction(Transaction t){
    if (t.source.id < t.target.id){
      synchronized (t.source){ //take lock 0
        synchronized (t.target){ //take lock 1
          t.transfer();
        }
      }
    }

    if (t.source.id > t.target.id){
      synchronized (t.target){ //take lock 1
        synchronized (t.source){ //take lock 3
          t.transfer();
        }
      }
    }
  }
  
  static class Transaction {
    final Account source, target;
    final long amount;
    Transaction(long amount, Account source, Account target){
      this.amount = amount;
      this.source = source;
      this.target = target;
    }
    
    public void transfer(){
      source.withdraw(amount);
      try{Thread.sleep(50);} catch(Exception e){}; // Simulate transaction time
      target.deposit(amount);
    }
    
    public String toString(){
      return "Transfer " + amount + " from " + source.id + " to " + target.id;
    }
  }

  static class Account{
    // should have transaction history, owners, account-type, and 100 other real things
    public final int id;
    private long balance = 0;
    Account( int id ){ this.id = id;}
    public void deposit(long sum){ balance += sum; } 
    public void withdraw(long sum){ balance -= sum; }
    public long getBalance(){ return balance; }
  }

}