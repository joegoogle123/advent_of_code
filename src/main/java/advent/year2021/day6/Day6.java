package advent.year2021.day6;

import advent.AdventConstants;
import advent.AdventOfCodeRunner;
import advent.AdventOfCodeSolver;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Day6 extends AdventOfCodeSolver {

    private List<Integer> initialReproductionSchedule;

    @Override
    public Optional<String> solvePart1() {
        long population = findLanternFishPopulation(initialReproductionSchedule, 80);
        return fromNumber(population);
    }

    @Override
    public Optional<String> solvePart2() {
        long population = findLanternFishPopulation(initialReproductionSchedule, 256);
        return fromNumber(population);
    }

    private List<Integer> findInitialReproductionSchedule() {
        if (initialReproductionSchedule == null) {
            return asStream().flatMap(s -> Arrays.stream(s.split(","))).map(Integer::valueOf).toList();
        } else {
            return initialReproductionSchedule;
        }
    }

    @Override
    protected void init() {
        this.initialReproductionSchedule = findInitialReproductionSchedule();
    }

    private long findLanternFishPopulation(List<Integer> initialReproductionSchedule, int iterations) {
        long[] frequencyArray = new long[9];

        for (Integer internalTimer : initialReproductionSchedule) {
            frequencyArray[internalTimer] += 1;
        }

        for (int i = 0; i < iterations; i++) {
            updateFrequency(frequencyArray);
        }

        return Arrays.stream(frequencyArray).sum();
    }


    /**
     * Shifts all frequencies to the left, representing a day has passed.
     * Take the frequency at index 0 and copy it over at index 8 representing newly reproduced fish
     * Take the frequency at index 0 and add it to the frequency at index 6 representing newly added fish that have 7 turns
     * until they will procreate once again
     *
     * @param frequencyArray: Frequency array ranging from index 0 to 8 representing the time left in days until a fish can reproduce
     * @return frequencyArray after a day has passed
     */
    private void updateFrequency(long[] frequencyArray) {
        // save and zero out when timer hits 0
        var newFishCount = frequencyArray[0];

        for (int i = 1; i < frequencyArray.length; i++) {
            frequencyArray[i - 1] = frequencyArray[i];
        }

        frequencyArray[6] += newFishCount;
        frequencyArray[8] = newFishCount;

    }

    public static void main(String[] args) {
        var runner = AdventOfCodeRunner.getRunner(AdventConstants.YEAR_2021, AdventConstants.DAY6);
        var solver = new Day6();
        runner.solve(solver);
    }
}
