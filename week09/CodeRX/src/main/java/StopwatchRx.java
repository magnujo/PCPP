import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

/* This example is inspired by the StopWatch app in Head First. Android Development
http://shop.oreilly.com/product/0636920029045.do
Modified to Java, October 2020 by Jørgen Staunstrup, ITU, jst@itu.dk */

public class StopwatchRx {

  private static stopwatchUI myUI;
 
  //Observable simulating clock ticking every second
    final static Observable<Integer> timer = Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(ObservableEmitter<Integer> e) throws Exception {
          Thread t = new Thread() {
            @Override
            public void run() {
              try {
                while ( true ) {
                  TimeUnit.SECONDS.sleep(1);
                  e.onNext(1);
                }
              } catch (InterruptedException e) {
                System.out.println(e.toString());
              }
            }
          }; 
          t.start();
        }
    });

  // Observer updating the display
  final static Observer<Integer> display= new Observer<Integer>() {
    @Override
    public void onSubscribe(Disposable d) {  }
    @Override
    public void onNext(Integer value) {
      myUI.updateTime();
    }
    @Override
    public void onError(Throwable e) {System.out.println("onError: "); }
    @Override
    public void onComplete() { System.out.println("onComplete: All Done!");   }
  };

	
	public static void main(String[] args) { 
    JFrame f=new JFrame("Stopwatch");  	
		f.setBounds(0, 0, 220, 220); 
    myUI= new stopwatchUI(0, f);
 
    //TO DO
    //Insert code using timer and display to make a working version of the Stopwatch
    timer.subscribe(display);
    f.setLayout(null);  
		f.setVisible(true);   
	}
}
