package advent.year2021.day3;

import advent.AdventConstants;
import advent.AdventOfCodeRunner;
import advent.AdventOfCodeSolver;
import advent.StreamUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.ToIntFunction;

public class Day3 extends AdventOfCodeSolver {

    @Override
    public Optional<String> solvePart1() {

        List<BitNumber> bitNumbers = asStream().map(Day3::toBitNumber).toList();
        var positions = bitNumbers.iterator().next().maxPosition;
        var bitFrequencies = new ArrayList<BitFrequency>(positions);
        for (int i = 0; i < positions; i++) {
            int finalI = i;
            BitFrequency frequency = bitNumbers.stream().map(s -> s.at(finalI)).reduce(BitFrequency.ZERO, BitFrequency::accumulateBit, StreamUtils.throwingBinaryOperator());
            bitFrequencies.add(frequency);
        }

        long gammaRate = computeGammaRate(bitFrequencies);
        long epsilonRate = computeEpsilon(bitFrequencies);
        return fromNumber(gammaRate * epsilonRate);
    }

    @Override
    public Optional<String> solvePart2() {
        List<BitNumber> bitNumbers = asStream().map(Day3::toBitNumber).toList();

        long oxygenRating = computeRating(new LinkedList<>(bitNumbers), frequency -> frequency.mostCommon(1));
        long co2Rating = computeRating(new LinkedList<>(bitNumbers), frequency -> frequency.leastCommon(0));

        return fromNumber(oxygenRating * co2Rating);
    }

    private int computeGammaRate(List<BitFrequency> bitFrequencies) {
        return bitFrequencies.stream()
                .mapToInt(BitFrequency::mostCommon)
                .reduce((current, next) -> (current << 1) ^ next).orElse(-1);
    }

    private int computeEpsilon(List<BitFrequency> bitFrequencies) {
        return bitFrequencies.stream()
                .mapToInt(BitFrequency::leastCommon)
                .reduce((current, next) -> (current << 1) ^ next).orElse(-1);
    }

    static BitNumber toBitNumber(String line)  {
        var value = Integer.parseInt(line, 2);
        return new BitNumber(value, line.length());
    }

    private int computeRating(LinkedList<BitNumber> bitNumbers, ToIntFunction<BitFrequency> bitCriteria) {
        int position = 0;
        while (bitNumbers.size() > 1) {
            final int tempPos = position;
            var bitFrequency = bitNumbers.stream().map(s -> s.at(tempPos)).reduce(BitFrequency.ZERO, BitFrequency::accumulateBit, StreamUtils.throwingBinaryOperator());
            int rating = bitCriteria.applyAsInt(bitFrequency);
            bitNumbers.removeIf(bitNumber -> bitNumber.at(tempPos) != rating);
            position++;
        }
        return bitNumbers.iterator().next().value();
    }

    static record BitNumber(int value, int maxPosition) {
        private static final int bitMask = 1;
        int at(int position) {
            return (value >> (maxPosition - position) - 1) & bitMask;
        }
    }

    private static record BitEvent(long position, int zeroOrOne) {}

    private static record BitFrequency(long zeroFrequency, long oneFrequency) {
        public static final BitFrequency ZERO = new BitFrequency(0,0);

        BitFrequency addEvent(BitEvent bitEvent) {
            return switch (bitEvent.zeroOrOne) {
                case 0 ->  new BitFrequency(this.zeroFrequency + 1, this.oneFrequency);
                case 1 -> new BitFrequency(this.zeroFrequency, this.oneFrequency + 1);
                default -> throw new IllegalArgumentException("The bit event " + bitEvent + " is not allowed");
            };
        }

        BitFrequency accumulateBit(int value) {
            return switch (value) {
                case 0 ->  new BitFrequency(this.zeroFrequency + 1, this.oneFrequency);
                case 1 -> new BitFrequency(this.zeroFrequency, this.oneFrequency + 1);
                default -> throw new IllegalArgumentException("The bit event " + value + " is not allowed");
            };
        }

        int mostCommon(int tieBreaker) {
            if (this.zeroFrequency > this.oneFrequency) {
                return 0;
            } else if (this.oneFrequency > this.zeroFrequency) {
                return 1;
            } else {
                return tieBreaker;
            }
        }

        int mostCommon() {
            return mostCommon(-1);
        }

        int leastCommon() {
         return leastCommon(-1);
        }

        int leastCommon(int tieBreaker) {
            if (this.zeroFrequency < this.oneFrequency) {
                return 0;
            } else if (this.oneFrequency < this.zeroFrequency) {
                return 1;
            } else {
                return tieBreaker;
            }
        }
    }

    public static void main(String[] args) {
        var runner = AdventOfCodeRunner.getRunner(AdventConstants.YEAR_2021, AdventConstants.DAY3);
        var solver = new Day3();
        runner.solve(solver);
    }
}
