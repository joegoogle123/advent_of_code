package advent.year2021.day5;

import advent.AdventConstants;
import advent.AdventOfCodeRunner;
import advent.AdventOfCodeSolver;
import advent.year2021.day4.Day4;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day5 extends AdventOfCodeSolver {

    @Override
    public Optional<String> solvePart1() {
        Stream<LineSegment> lineSegments = asStream().map(this::parseAsLineSegment).filter(LineSegment::isHorizontalOrVertical);
        Map<Point, Long> pointFrequency = lineSegments.flatMap(LineSegment::asPoints)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        var numberOfOverlaps = pointFrequency.values().stream().filter(count -> count >= 2).count();
        return fromNumber(numberOfOverlaps);
    }

    @Override
    public Optional<String> solvePart2() {
        Stream<LineSegment> lineSegments = asStream().map(this::parseAsLineSegment);
        Map<Point, Long> pointFrequency = lineSegments.flatMap(LineSegment::asPoints)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        var numberOfOverlaps = pointFrequency.values().stream().filter(count -> count >= 2).count();
        return fromNumber(numberOfOverlaps);
    }

    private static record Point(int x, int y) {}

    private static record LineSegment(Point start, Point end) {

        boolean isHorizontalOrVertical() {
            return lineIsVertical() || lineIsHorizontal();
        }

        private boolean lineIsVertical() {
            return start.x == end.x;
        }

        private boolean lineIsHorizontal() {
            return start.y == end.y;
        }

        private int slope() {
            return (start.y - end.y) / (start.x - end.x);
        }

        IntFunction<Point> constructLinearPointFunction() {
            var slope = slope();
            var yIntercept = start.y - start.x * slope;
            IntUnaryOperator linearFunction = (int x) -> slope * x + yIntercept;
            return x -> {
                var y = linearFunction.applyAsInt(x);
                return new Point(x, y);
            };
        }

        public Stream<Point> asPoints() {
            //  m is undefined when we have a vertical line, so we need to iterate over y values
            if (lineIsVertical()) {
                var constant = start.x;
                var yCoordinates = IntStream.rangeClosed(Math.min(start.y, end.y), Math.max(start.y, end.y));
                return yCoordinates.mapToObj(y -> new Point(constant, y));
            } else {
                IntFunction<Point> linearPointFunction = constructLinearPointFunction();
                var xCoordinates = IntStream.rangeClosed(Math.min(start.x, end.x), Math.max(start.x, end.x));
                return xCoordinates.mapToObj(linearPointFunction);
            }
        }
    }

    private LineSegment parseAsLineSegment(String line) {
        String[] tokens = line.split(" -> ");

        int[] startArr = Arrays.stream(tokens[0].split(",")).mapToInt(Integer::parseInt).toArray();
        var start = new Point(startArr[0], startArr[1]);

        int[] endArr = Arrays.stream(tokens[1].split(",")).mapToInt(Integer::parseInt).toArray();
        var end = new Point(endArr[0], endArr[1]);

        return new LineSegment(start, end);
    }

    public static void main(String[] args) {
        var runner = AdventOfCodeRunner.getRunner(AdventConstants.YEAR_2021, AdventConstants.DAY4);
        var solver = new Day4();
        runner.solve(solver);
    }
}
