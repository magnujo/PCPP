import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

public class FileReaderTester {

    static BufferedReader reader;

    static {
        try {
            reader = new BufferedReader(new FileReader("C:\\Users\\Magnus\\IdeaProjects\\Examtraining\\week09\\CodeRX\\src\\main\\java\\english-words.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    final static Observable<String> readWords = Observable.create(new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(ObservableEmitter<String> s) throws Exception {
            try {
                s.onNext(reader.readLine());

                // TO DO: Implement properly

            } catch (IOException exn) { }
        }
    });

    final static Observer<String> display = new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(String s) {
            System.out.println(s);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    public static void main(String[] args) {
        System.out.println("hej");
        for (int i = 0; i < 100; i++) {
            readWords.subscribe(display);
        }
    }





}
