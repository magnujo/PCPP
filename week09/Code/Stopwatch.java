import java.awt.event.*;  
import javax.swing.*; 
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/* This example is inspired by the StopWatch app in Head First. Android Development
http://shop.oreilly.com/product/0636920029045.do

Modified to Java, October 2020 by Jørgen Staunstrup, ITU, jst@itu.dk */

public class Stopwatch {

  private static stopwatchUI myUI;
	
	public static void main(String[] args) { 
		JFrame f=new JFrame("Stopwatch");  	
		f.setBounds(0, 0, 220, 220); 
    myUI= new stopwatchUI(0, f);
 
    f.setLayout(null);  
		f.setVisible(true); 
		
		Thread t= new Thread() {
			private int seconds= 0;
			// Background Thread simulation a clock ticking every 1 seconde
			@Override
			public void run() {
				int temp= 0;
				try {
					while ( true ) {
						TimeUnit.SECONDS.sleep(1);
						myUI.updateTime();
					}
				} catch (java.lang.InterruptedException e) {
					System.out.println(e.toString());
				}
			}
		}; 
    t.start();
	}
}
