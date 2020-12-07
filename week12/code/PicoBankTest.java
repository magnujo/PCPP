import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

class PicoBankTest {
  private static final int N = 10; // Number of bank accounts in bank
  public static void main(String[] args) throws BrokenBarrierException, InterruptedException {

  /*  for (int i = 0; i < 10; i++) {
      testBankParallel(new PicoBankSmartSync( N ));
    }

    for (int i = 0; i < 10; i++) {
      testBankParallel(new PicoBankBasicSync( N ));
    }

    for (int i = 0; i < 10; i++) {
      testBankSequential(new PicoBankBasic( N ));
    }*/

    for (int i = 0; i < 10; i++) {
      testBankParallel(new PicoBankAtomicBasic( N ));
    }




  }



  
  private static void testBankSequential(PicoBank bank){
    long start = System.nanoTime();
    for (int i=0; i< 1_000_000; i++)
      doRandomTransfer(bank);
    long time = System.nanoTime() - start;
    long sum = 0L;
    for (int i=0; i<N; i++) sum += bank.balance(i);
    System.out.println("Single thread test: " + (sum == 0 ? "SUCCESS" : "FAILURE"));
    System.out.printf("Single thread time: %,dns\n", time );
  }

  private static void testBankParallel(PicoBank bank) throws BrokenBarrierException, InterruptedException {
    final int numOfCores = 4;
    final int numOfThreads = numOfCores*4;
    final int reps = 1_000_000;
    final CyclicBarrier barrier = new CyclicBarrier(numOfThreads+1);

    Runnable task = () -> {
      try {
        barrier.await();
        for (int i = 0; i < reps; i++) doRandomTransfer(bank);
        barrier.await();
      }
      catch (Exception e) {}
    };

    for (int i = 0; i < numOfThreads; i++) {
      new Thread(task).start();
    }

    barrier.await();
    long start = System.nanoTime();
    barrier.await();
    long time = System.nanoTime() - start;

    long sum = 0L;
    for (int i=0; i<N; i++) sum += bank.balance(i);
    System.out.println("Multiple thread test: " + (sum == 0 ? "SUCCESS" : "FAILURE"));
    System.out.printf("Multiple thread time: %,dns\n", time );
  }
    

  static final Random rnd = new Random(); // replace this with efficient Random
  public static void doRandomTransfer(PicoBank bank){
    long amount = rnd.nextInt(5000)+100; // Just a random possitive amount
    int source = rnd.nextInt(N);
    int target = (source + rnd.nextInt(N-2)+1) % N; // make sure target <> source
    bank.transfer(amount, source, target);
  }
}

////////////////////
//PicoBank interface
////////////////////

interface PicoBank {
  void transfer(long amount, int source, int target);
  long balance(int accountNr);
}

/////////////////////
//PicoBankBasic class
/////////////////////

class PicoBankBasic implements PicoBank{
  final int N; // Number of accounts
  final Account[] accounts ;

  //Initializing n accounts in an array.
  PicoBankBasic(int noAccounts){
    N = noAccounts;
    accounts = new Account[N];
    for( int i = 0; i < N; i++){
      accounts[i] = new Account(i);
    }
  }
  
  public void transfer(long amount, int source, int target){
      accounts[source].withdraw(amount);
      accounts[target].deposit(amount);
  }
  public long balance(int accountNr){
    return accounts[accountNr].getBalance();
  } 

  static class Account{
    // should have transaction history, owners, account-type, and 100 other real things
    public final int id;
    private long balance = 0;
    Account( int id ){ this.id = id;}
    public void deposit(long sum){ balance += sum; } 
    public void withdraw(long sum){ balance -= sum; }
    public long getBalance(){
      return balance; }
  }
}

class PicoBankBasicSync implements PicoBank{
  final int N; // Number of accounts
  final Account[] accounts ;

  //Initializing n accounts in an array.
  PicoBankBasicSync(int noAccounts){
    N = noAccounts;
    accounts = new Account[N];
    for( int i = 0; i < N; i++){
      accounts[i] = new Account(i);
    }
  }

  synchronized public void transfer(long amount, int source, int target){
    accounts[source].withdraw(amount);
    accounts[target].deposit(amount);
  }
  public long balance(int accountNr){
    return accounts[accountNr].getBalance();
  }

  static class Account{
    // should have transaction history, owners, account-type, and 100 other real things
    public final int id;
    private long balance = 0;
    Account( int id ){ this.id = id;}
    public void deposit(long sum){ balance += sum; }
    public void withdraw(long sum){ balance -= sum; }
    public long getBalance(){
      return balance; }
  }
}

class PicoBankSmartSync implements PicoBank{
  final int N; // Number of accounts
  final Account[] accounts ;

  //Initializing n accounts in an array.
  PicoBankSmartSync(int noAccounts){
    N = noAccounts;
    accounts = new Account[N];
    for( int i = 0; i < N; i++){
      accounts[i] = new Account(i);
    }
  }

  synchronized public void transfer(long amount, int source, int target){
     Account s = accounts[source];
     Account t = accounts[target];


    if (accounts[source].id < accounts[target].id){
      synchronized (s){
        synchronized (t){
          accounts[source].withdraw(amount);
          accounts[target].deposit(amount);
        }
      }
    }

    if (accounts[source].id > accounts[target].id){
      synchronized (t){
        synchronized (s){
          accounts[source].withdraw(amount);
          accounts[target].deposit(amount);
        }
      }
    }

  }
  public long balance(int accountNr){
    return accounts[accountNr].getBalance();
  }

  static class Account{
    // should have transaction history, owners, account-type, and 100 other real things
    public final int id;
    private long balance = 0;
    Account( int id ){ this.id = id;}
    public void deposit(long sum){ balance += sum; }
    public void withdraw(long sum){ balance -= sum; }
    public long getBalance(){
      return balance; }
  }


}

class PicoBankAtomic implements PicoBank{
  final int N; // Number of accounts
  final Account[] accounts ;

  //Initializing n accounts in an array.
  PicoBankAtomic(int noAccounts){
    N = noAccounts;
    accounts = new Account[N];
    for( int i = 0; i < N; i++){
      accounts[i] = new Account(i);
    }
  }

  public void transfer(long amount, int source, int target){
    //From 1 to 0 and from 0 to 1
    accounts[source].withdraw(amount);
    accounts[target].deposit(amount);
  }
  public long balance(int accountNr){
    return accounts[accountNr].getBalance();
  }

  static class Account{
    // should have transaction history, owners, account-type, and 100 other real things
    public final int id;
    private AtomicLong balance = new AtomicLong(0);
    Account( int id ){ this.id = id;}
    public void deposit(long sum){ balance.addAndGet(sum); }
    public void withdraw(long sum){ balance.addAndGet(-sum); }
    public long getBalance(){
      System.out.println(balance.get());
      return balance.get(); }
  }
}


class PicoBankAtomicBasic implements PicoBank{
  final int N; // Number of accounts
  final Account[] accounts ;

  //Initializing n accounts in an array.
  PicoBankAtomicBasic(int noAccounts){
    N = noAccounts;
    accounts = new Account[N];
    for( int i = 0; i < N; i++){
      accounts[i] = new Account(i);
    }
  }

  public void transfer(long amount, int source, int target){
    //From 1 to 0 and from 0 to 1
    accounts[source].withdraw(amount);
    accounts[target].deposit(amount);
  }
  public long balance(int accountNr){
    return accounts[accountNr].getBalance();
  }

  static class Account{
    // should have transaction history, owners, account-type, and 100 other real things
    public final int id;
    private AtomicLong balance = new AtomicLong(0);
    Account( int id ){ this.id = id;}
    public void deposit(long sum){
      long curBal = balance.get();

      while(!balance.compareAndSet(curBal,curBal+sum)){
        curBal = balance.get();
      }
    }
    public void withdraw(long sum) {
      long curBal = balance.get();

      while (!balance.compareAndSet(curBal, curBal - sum)) {
        curBal = balance.get();
      }
    }

    public long getBalance(){
      return balance.get(); }
  }
}