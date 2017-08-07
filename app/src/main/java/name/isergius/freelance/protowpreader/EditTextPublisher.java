package name.isergius.freelance.protowpreader;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayDeque;
import java.util.Queue;

import io.reactivex.exceptions.MissingBackpressureException;

/**
 * @author Sergey Kondratyev
 */

public class EditTextPublisher implements TextWatcher, Publisher<CharSequence> {

    private static final String TAG = EditTextPublisher.class.getSimpleName();

    private Subscriber<? super CharSequence> subscriber;
    private Queue<CharSequence> buffer = new ArrayDeque<>(16);
    private long count;

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (subscriber != null) {
            try {
                buffer.add(editable);
                publish();
            } catch (IllegalStateException e) {
                throw new MissingBackpressureException();
            }
        }
    }

    @Override
    public void subscribe(Subscriber<? super CharSequence> s) {
        subscriber = s;
        subscriber.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                Log.i(TAG, "request: " + n);
                count = n;
                publish();
            }

            @Override
            public void cancel() {
                subscriber = null;
            }
        });
    }

    private void publish() {
        for (long i = 0; i < count && !buffer.isEmpty(); i++) {
            if (buffer.isEmpty()) {
                return;
            } else {
                subscriber.onNext(buffer.remove());
                count--;
            }
        }
    }
}
