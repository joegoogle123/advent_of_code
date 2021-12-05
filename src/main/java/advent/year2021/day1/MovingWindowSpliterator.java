package advent.year2021.day1;

import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MovingWindowSpliterator<T> implements Spliterator<List<T>> {

    private final int windowSize;
    private final LinkedList<T> window;
    private final Spliterator<T> spliterator;
    private final Consumer<T> appendingConsumer = new AppendingConsumer();
    private final Consumer<T> slidingConsumer = new SlidingConsumer();

    MovingWindowSpliterator(int windowSize, Spliterator<T> spliterator) {
        this.window = new LinkedList<>();
        this.windowSize = windowSize;
        this.spliterator = spliterator;
    }

    public static <T> Stream<List<T>> movingWindow(int windowSize, Stream<T> stream) {
        if (stream.isParallel()) throw new RuntimeException("Stream cannot be parallel");
        return StreamSupport.stream(new MovingWindowSpliterator<>(windowSize, stream.spliterator()), false);
    }

    private class AppendingConsumer implements Consumer<T> {
        @Override
        public void accept(T t) {
            window.add(t);
        }
    }

    private class SlidingConsumer implements Consumer<T> {
        @Override
        public void accept(T t) {
            window.poll();
            window.add(t);
        }
    }

    @Override
    public boolean tryAdvance(Consumer<? super List<T>> action) {
        boolean innerSpliterator = true;

        if (window.isEmpty()) {
            while (innerSpliterator && window.size() < windowSize) {
                innerSpliterator = spliterator.tryAdvance(appendingConsumer);
            }

            if (window.size() < windowSize) {
                action.accept(List.copyOf(window));
                return false;
            }
        } else {
            innerSpliterator = spliterator.tryAdvance(slidingConsumer);
        }

        if (innerSpliterator) {
            action.accept(List.copyOf(window));
        }

        return innerSpliterator;
    }

    @Override
    public Spliterator<List<T>> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return 0;
    }

    @Override
    public int characteristics() {
        return 0;
    }
}
