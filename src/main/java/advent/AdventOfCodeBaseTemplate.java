package advent;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.MonthDay;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class AdventOfCodeBaseTemplate {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();

    private final Year year;
    private final MonthDay day;
    protected final Path inputPath;
    private final List<String> input;

    public AdventOfCodeBaseTemplate(Year year, MonthDay day) {
        this.year = year;
        this.day = day;

        var inputFetcher = new AdventOfCodeInputFetcher(HTTP_CLIENT, SessionId.loadFromResource().get());
        this.inputPath = inputFetcher.fetchInput(year, day);
        try {
            this.input = Files.readAllLines(inputPath);
        } catch (IOException e) {
            throw new RuntimeException("cannot load file");
        }
    }

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

    public abstract Optional<String> answerForPart1();
    public abstract Optional<String> answerForPart2();

    public void run() {
        System.out.println("Running advent of code Year: %d Day: %d".formatted(year.getValue(), day.getDayOfMonth()));
        System.out.println("------------------------------------");
        System.out.println("Running part 1");
        var part1 = time(this::answerForPart1);
        part1.getKey().ifPresent(answer -> {
            System.out.println("Solution to part 1:%n%s".formatted(answer));
            System.out.println("Measured time %d (ms)".formatted(part1.getValue().toMillis()));
        });
        System.out.println("------------------------------------");

        var part2 = time(this::answerForPart2);
        part2.getKey().ifPresent(answer -> {
            System.out.printf("Solution to part 2:%n%s\n", answer);
            System.out.println("Measured time %d (ms)".formatted(part1.getValue().toMillis()));
        });
    }

    private static <E> Map.Entry<E, Duration> time(Supplier<E> task) {
        var before = System.nanoTime();
        E output = task.get();
        var after = System.nanoTime();
        return Map.entry(output, Duration.ofNanos(after - before));
    }
}
