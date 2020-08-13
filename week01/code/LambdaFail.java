// lambda fail
// This code cannot compile. Java does not allow references to non-final local variables
import java.util.function.*;

public class LambdaFail {
  public static void main(String[] args) {
    IntSupplier myLambda = incrementer(10);
		System.out.println("First value: " + myLambda );
		System.out.println("Second value: " + myLambda );
	}
	
	private static IntSupplier incrementer(int start){
		return ()->start++;
	}
}