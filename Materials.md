### Mandatory course materials

The list of reading materials will be updated throughout the semester. However, you are only required to get a copy of this one book, other material will be made available at Learnit or through links.

## Mandatory book

-   Goetz et al: Java Concurrency in Practice, Addison-Wesley 2006. The Academic Bookstore will sell it at ITU at start of term. Also [at Amazon.co.uk](http://www.amazon.co.uk/Java-Concurrency-Practice-Brian-Goetz/dp/0321349601). Example code at [http://jcip.net/](http://jcip.net/).

- Yes, the book is old, but unfortunately still the best book around.

------

### Resources
The internet is full of ressources, youtubes, blogs etc. Consider reading the original material as part of your academic schooling.

-   Java concurrency tutorial. Free at [http://docs.oracle.com/javase/tutorial/essential/concurrency/](http://docs.oracle.com/javase/tutorial/essential/concurrency/).
-   Java streams tutorial, parallelism section. Free at [http://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html](http://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html).
-   Java Swing GUI Toolkit tutorial, concurrency lesson at [http://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html](http://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html).
-   Java 8 [class library documentation online](http://docs.oracle.com/javase/8/docs/api/).
-   Java 8 [development kit download](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
-   Java 8 language specification [in PDF](http://docs.oracle.com/javase/specs/jls/se8/jls8.pdf) and [online as HTML](http://docs.oracle.com/javase/specs/jls/se8/html/index.html); especially [17.1 synchronization](http://docs.oracle.com/javase/specs/jls/se8/html/jls-17.html#jls-17.1) and [8.3.1.4 volatile fields](http://docs.oracle.com/javase/specs/jls/se8/html/jls-8.html#jls-8.3.1.4) and [17.5 final field semantics](http://docs.oracle.com/javase/specs/jls/se8/html/jls-17.html#jls-17.5) and the somewhat complicated [17.4 memory model](http://docs.oracle.com/javase/specs/jls/se8/html/jls-17.html#jls-17.4).
-   Brian Goetz about the design of the Java 1.5 memory model: [part 1](http://www.ibm.com/developerworks/java/library/j-jtp02244/index.html) and [part 2](https://www.ibm.com/developerworks/library/j-jtp03304/), IBM 2004.
-   Java Concurrency thread-safety annotations [jar file](http://jcip.net/jcip-annotations.jar) for @GuardedBy and similar. Download the jar file, put it someplace such as `~/lib/jcip-annotations.jar`, add `import javax.annotation.concurrent.GuardedBy;` to your Java source files, and mention the jar file on the classpath when compiling Java files, as in `javac -cp ~/lib/jcip-annotaitons.jar *.java`
