public class SecCounter {
  public int seconds;
  private boolean running;

  public SecCounter(int s, boolean r){
    seconds= s;
    running= r;
    //System.out.println("Init " + running + seconds);
  }
	
  public void setRunning(boolean running) {
    this.running= running;
  }

  public boolean incr(){
    if (running) { seconds++; 
      //System.out.println("incr " + running + seconds); 
    }
    return running;
  }

  public boolean running(){
    return this.running;
  }
}
