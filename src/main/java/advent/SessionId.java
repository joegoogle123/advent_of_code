package advent;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

public record SessionId(String session) {


    private static final String SESSION_JSON = "secrets.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Optional<SessionId> loadFromResource() {
        try {
            InputStream inputStream = Objects.requireNonNull(SessionId.class.getClassLoader().getResourceAsStream(SESSION_JSON),
                                                    "Please add a secrets secrets.json into your resources folder");
            return Optional.of(objectMapper.readValue(inputStream, SessionId.class));
        } catch (Exception e) {
            System.out.println("Failed to load session");
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
