import java.util.Random;

class PicoBankTest {
  private static final int N = 10; // Number of bank accounts in bank
  public static void main(String[] args){
    testBankSequential(new PicoBankBasic( N ));
  }
  
  private static void testBankSequential(PicoBank bank){
    long start = System.nanoTime();
    for (int i=0; i< 10_000; i++)
      doRandomTransfer(bank);
    long time = System.nanoTime() - start;
    long sum = 0L;
    for (int i=0; i<N; i++) sum += bank.balance(i);
    System.out.println("Single thread test: " + (sum == 0 ? "SUCCESS" : "FAILURE"));
    System.out.printf("Single thread time: %,dns\n", time );
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
    public long getBalance(){ return balance; }
  }
}