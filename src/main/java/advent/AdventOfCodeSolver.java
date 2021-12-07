package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class AdventOfCodeSolver {

    private List<String> input;

    private void loadInput(Path path) {
        if (input != null) System.out.println("Already cached in memory");

        try {
            this.input = Files.readAllLines(path);
        } catch (IOException e) {
            System.out.println("Could not load path " + path);
            throw new RuntimeException(e);
        }
    }

    protected void init() {}

    protected List<String> asList() {
        return input;
    }

    protected Stream<String> asStream() {
        return input.stream();
    }

    protected Optional<String> fromNumber(long solution) {
        return Optional.of(solution).map(String::valueOf);
    }

    protected Optional<String> fromNumber(int solution) {
        return Optional.of(solution).map(String::valueOf);
    }

    /**
     * Implement this method for advent of code problem part 1
     * @return Solution to the first problem
     */
    public abstract Optional<String> solvePart1();

    /**
     * Implement this method for advent of code problem part 2
     * @return Solution to the first problem
     */
    public abstract Optional<String> solvePart2();

    /**
     * Runs both parts and time theirs running time and prints out the solutions.
     */

    public void solve(Path inputPath) {

        loadInput(inputPath);
        init();

        System.out.println("------------------------------------");
        System.out.println("Running part 1");

        var part1 = time(this::solvePart1);

        part1.getKey().ifPresent(answer -> {
            System.out.println("Solution to part 1:%n%s".formatted(answer));
            System.out.println("Measured time %d (ms)".formatted(part1.getValue().toMillis()));
        });
        System.out.println("------------------------------------");

        var part2 = time(this::solvePart2);

        part2.getKey().ifPresent(answer -> {
            System.out.printf("Solution to part 2:%n%s%n", answer);
            System.out.printf("Measured time %d (ms)%n", part2.getValue().toMillis());
        });
    }

    private static <E> Entry<E, Duration> time(Supplier<E> task) {
        var before = System.nanoTime();
        E output = task.get();
        var after = System.nanoTime();
        return Map.entry(output, Duration.ofNanos(after - before));
    }
}
