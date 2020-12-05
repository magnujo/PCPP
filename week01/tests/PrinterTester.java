class Printer {
    public void print(){
        System.out.print("-");
        try{Thread.sleep(50);} catch (InterruptedException exn){}
        System.out.print("|");
    }
}

public class PrinterTester{
    public static void main (String[] args){
        Printer p = new Printer();

        Thread t1 = new Thread(() -> {
            while (true) {
                p.print();
            }
        });

        Thread t2 = new Thread(() -> {
            while (true) {
                p.print();
            }
        });

        t1.start();
        t2.start();

    }
}
