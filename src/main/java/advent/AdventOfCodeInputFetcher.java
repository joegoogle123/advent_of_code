package advent;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.*;
import java.time.MonthDay;
import java.time.Year;

public class AdventOfCodeInputFetcher {

    private static final String DOWNLOAD_LINK = "https://adventofcode.com/%d/day/%d/input";
    private static final Path resourcePath = Paths.get("src","main","resources");

    public final HttpClient httpClient;
    private final SessionId sessionId;


    public AdventOfCodeInputFetcher(HttpClient httpClient, SessionId sessionId) {
        this.httpClient = httpClient;
        this.sessionId = sessionId;
    }

    public Path fetchInput(Year year, MonthDay day)  {
        System.out.println("Fetching input...");
        var resolvedUri = createLink(year, day);
        var destinationPath = createFilePath(year, day);
        // make year directory if not existing
        try {
            Path yearDirectory = destinationPath.getParent();
            if (Files.isDirectory(yearDirectory)) {
                System.out.printf("Directory %s already exists. Will not attempt to make new directory%n", yearDirectory);
            } else {
                Files.createDirectory(yearDirectory);
            }

            if (Files.exists(destinationPath)) {
                System.out.printf("input file %s already exists. Will not attempt to download file%n", yearDirectory);
                return destinationPath;
            } else {
                return downloadFileToResources(resolvedUri, destinationPath).body();
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public URI createLink(Year year, MonthDay day) {
        return URI.create(DOWNLOAD_LINK.formatted(year.getValue(), day.getDayOfMonth()));
    }

    public static Path createFilePath(Year year, MonthDay day) {
        return resourcePath
                .resolve(String.valueOf(year.getValue()))
                .resolve("input%d.txt".formatted(day.getDayOfMonth()));
    }

    private HttpRequest createRequest(URI downloadLink) {
        return HttpRequest.newBuilder(downloadLink)
                .header("cookie","session=%s".formatted(sessionId.session()))
                .GET().build();
    }

    public HttpResponse<Path> downloadFileToResources(URI downloadURI ,Path downloadDestinationPath) throws IOException, InterruptedException {
        var downloadHandler = HttpResponse.BodyHandlers.ofFile(downloadDestinationPath);
        var httpRequest = createRequest(downloadURI);
        return httpClient.send(httpRequest, downloadHandler);
    }
}
