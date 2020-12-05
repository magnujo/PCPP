public class MyAtomicInteger {
    private int value;
    private final Object lock = new Object();

    MyAtomicInteger(int value){
        this.value = value;
    }

    public int get(){
        return value;
    }

    public int addAndGet(int amount){
        synchronized (lock){
            value = value + amount;
            return value;
        }


    }
}
