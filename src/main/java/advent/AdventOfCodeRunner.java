package advent;

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.time.MonthDay;
import java.time.Year;

public class AdventOfCodeRunner {

    private static final HttpClient httpClient = HttpClient.newBuilder().build();
    private static final SessionId session = SessionId.loadFromResource().get();

    private final Year year;
    private final MonthDay day;
    private final AdventOfCodeInputFetcher adventOfCodeInputFetcher;

    private AdventOfCodeRunner(Year year, MonthDay day, AdventOfCodeInputFetcher adventOfCodeInputFetcher) {
        this.year = year;
        this.day = day;
        this.adventOfCodeInputFetcher = adventOfCodeInputFetcher;
    }

    public static AdventOfCodeRunner getRunner(Year year, MonthDay day) {
        var fetcher = new AdventOfCodeInputFetcher(httpClient, session);
        return new AdventOfCodeRunner(year, day, fetcher);
    }

    public void solve(AdventOfCodeSolver solver) {
        System.out.println("Running advent of code Year: %d Day: %d".formatted(year.getValue(), day.getDayOfMonth()));
        Path path = adventOfCodeInputFetcher.fetchInput(year, day);
        solver.solve(path);
    }
}
