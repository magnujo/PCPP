import java.util.concurrent.LinkedBlockingQueue;

public class MessageBufferTester {
    public static void main(String[] args) {

    }

}

class MyMessageBuffer<T> implements MessageBuffer<T>{
    LinkedBlockingQueue<T> q;

    public MyMessageBuffer(){
        this.q = new LinkedBlockingQueue<>();
    }

    @Override
    public void sendMessage(T elem) {
        try {
            q.put(elem);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T receiveMessage() {
        try {
            q.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

interface MessageBuffer<T>{
    void sendMessage(T elem);
    T receiveMessage();
}