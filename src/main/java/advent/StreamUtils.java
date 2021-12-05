package advent;

import java.util.function.BinaryOperator;

public class StreamUtils {

    private StreamUtils() {}

    public static <T> BinaryOperator<T> throwingBinaryOperator() {
        return  (c1, c2) -> {throw new RuntimeException();};
    }
}
