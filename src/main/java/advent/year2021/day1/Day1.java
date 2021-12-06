package advent.year2021.day1;

import advent.AdventConstants;
import advent.AdventOfCodeRunner;
import advent.AdventOfCodeSolver;

import java.time.MonthDay;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

public class Day1 extends AdventOfCodeSolver {

    private MonthDay day;

    long increasingCount(LongStream stream) {
        AtomicLong counter = new AtomicLong();
        stream.reduce((old, next) -> {
            if (old < next) {
                counter.incrementAndGet();
            }
            return next;
        });

        return counter.get();
    }

    @Override
    public Optional<String> solvePart1() {
        var input = asStream().mapToLong(Long::parseLong);
        var count = increasingCount(input);
        return fromNumber(count);
    }

    @Override
    public Optional<String> solvePart2() {
        var input = asStream().map(Long::valueOf);
        var movingSum = MovingWindowSpliterator.movingWindow(3, input).mapToLong(l -> l.stream().mapToLong(s -> s).sum());
        var count = increasingCount(movingSum);
        return fromNumber(count);
    }

    public static void main(String[] args) {
        var runner = AdventOfCodeRunner.getRunner(AdventConstants.YEAR_2021, AdventConstants.DAY1);
        var solver = new Day1();
        runner.solve(solver);
    }
}
