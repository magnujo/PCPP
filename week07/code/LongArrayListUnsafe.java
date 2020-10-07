// For week 7
// sestoft@itu.dk * 2015-10-29
// Changes kasper@itu.dk * 2020-10-05

import java.util.stream.*;
import java.util.Arrays;
public class LongArrayListUnsafe {
  public static void main(String[] args) {
    LongArrayList dal1 = LongArrayList.withElements(42, 7, 9, 13);
    dal1.set(2, 11);
    for (int i=0; i<dal1.size(); i++)
      System.out.println(dal1.get(i));
    System.out.println("Using toString(): " + dal1);
  }
}

class LongArrayList {
  // Invariant: 0 <= size <= items.length
  private long[] items;
  private int size;

  public LongArrayList() {
    reset();
  }
  
  public static LongArrayList withElements(long... initialValues){
    LongArrayList list = new LongArrayList();
    for (long l : initialValues) list.add( l );
    return list;
  }
  
  // reset me to initial 
  public void reset(){
    items = new long[2];
    size = 0;
  }

  // Number of items in the double list
  public int size() {
    return size;
  }

  // Return item number i
  public long get(int i) {
    if (0 <= i && i < size) 
      return items[i];
    else 
      throw new IndexOutOfBoundsException(String.valueOf(i));
  }

    // Replace item number i, if any, with x
  public long set(int i, long x) {
    if (0 <= i && i < size) {
      long old = items[i];
      items[i] = x;
      return old;
    } else 
      throw new IndexOutOfBoundsException(String.valueOf(i));
  }

  // Add item x to end of list
  public LongArrayList add(long x) {
    if (size == items.length) {
      long[] newItems = new long[items.length * 2];
      for (int i=0; i<items.length; i++)
	      newItems[i] = items[i];
      items = newItems;
    }
    items[size] = x;
    size++;
    return this;
  }


  // The long list formatted as eg "[3, 4]"
  // Just messing with stream joining - not part of solution
  public String toString() {
    return Arrays.stream(items, 0,size)
      .mapToObj( Long::toString )
      .collect(Collectors.joining(", ", "[", "]"));
  }
}
