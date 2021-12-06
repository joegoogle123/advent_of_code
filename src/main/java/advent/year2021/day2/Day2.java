package advent.year2021.day2;

import advent.AdventConstants;
import advent.AdventOfCodeRunner;
import advent.AdventOfCodeSolver;
import advent.StreamUtils;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Day2 extends AdventOfCodeSolver {

    @Override
    public Optional<String> solvePart1() {
        Stream<SubmarineEvent> submarineDirectionalEvents = asStream().map(this::toEvent);
        SubmarineEventHandler eventHandler = new PositionUpdateEventHandler();
        var finalState = submarineDirectionalEvents.reduce(SubmarineState.ZERO, stateAccumalator(eventHandler), StreamUtils.throwingBinaryOperator());
        return Optional.of(finalState).map(p -> p.horizontal() * p.depth()).map(String::valueOf);
    }

    @Override
    public Optional<String> solvePart2() {
        Stream<SubmarineEvent> submarineDirectionalEvents = asStream().map(this::toEvent);
        SubmarineEventHandler eventHandler = new PositionAndAimUpdateEventHandler();
        var finalState = submarineDirectionalEvents.reduce(SubmarineState.ZERO, stateAccumalator(eventHandler), StreamUtils.throwingBinaryOperator());
        return Optional.of(finalState).map(p -> p.horizontal() * p.depth()).map(String::valueOf);
    }

    private static record SubmarineEvent(Direction direction, long delta) {}

    private BiFunction<SubmarineState, SubmarineEvent, SubmarineState> stateAccumalator(SubmarineEventHandler eventHandler) {
        return (SubmarineState state, SubmarineEvent event) -> {
            SubmarineDelta delta = eventHandler.handleEvent(state, event);
            return state.applyDelta(delta);
        };
    }

    private SubmarineEvent toEvent(String line) {
        String[] tokens = line.split(" ");
        var direction = Direction.valueOf(tokens[0].toUpperCase());
        long delta = Long.parseLong(tokens[1]);
        return new SubmarineEvent(direction, delta);
    }

    interface SubmarineEventHandler {
        SubmarineDelta handleEvent(SubmarineState submarineState, SubmarineEvent submarineEvent);
    }

    private static class PositionUpdateEventHandler implements SubmarineEventHandler {

        @Override
        public SubmarineDelta handleEvent(SubmarineState submarineState, SubmarineEvent submarineEvent) {
            var delta = submarineEvent.delta;
            return switch (submarineEvent.direction) {
                case FORWARD -> new SubmarineDelta(delta, 0L, 0L);
                case DOWN -> new SubmarineDelta(0L, delta, 0L);
                case UP -> new SubmarineDelta(0L, -delta, 0L);
            };
        }
    }

    private static class PositionAndAimUpdateEventHandler implements SubmarineEventHandler {

        @Override
        public SubmarineDelta handleEvent(SubmarineState submarineState, SubmarineEvent submarineEvent) {
            var delta = submarineEvent.delta;
            return switch (submarineEvent.direction) {
                case FORWARD -> new SubmarineDelta(delta, delta * submarineState.aim(), 0L);
                case DOWN -> new SubmarineDelta(0L, 0L, delta);
                case UP -> new SubmarineDelta(0L, 0L, -delta);
            };
        }
    }

    private static record SubmarineDelta(long deltaH, long deltaD, long deltaA) {}


    private static record SubmarineState(long horizontal, long depth, long aim) {

        static final SubmarineState ZERO = new SubmarineState(0L,0L, 0L);

        public SubmarineState applyDelta(SubmarineDelta submarineDelta) {
            return new SubmarineState(
                    this.horizontal + submarineDelta.deltaH(),
                    this.depth + submarineDelta.deltaD(),
                    this.aim + submarineDelta.deltaA()
                    );
        }
    }

    private enum Direction {
        FORWARD, DOWN, UP
    }

    public static void main(String[] args) {
        var runner = AdventOfCodeRunner.getRunner(AdventConstants.YEAR_2021, AdventConstants.DAY2);
        var solver = new Day2();
        runner.solve(solver);
    }


}
