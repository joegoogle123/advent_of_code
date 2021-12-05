package advent.year2021.day4;

import advent.AdventConstants;
import advent.AdventOfCodeBaseTemplate;

import java.time.MonthDay;
import java.time.Year;
import java.util.*;

public class Day4 extends AdventOfCodeBaseTemplate {

    private List<BingoBoard> bingoBoards;
    private int[] bingoNumbers;

    public Day4(Year year, MonthDay day) {
        super(year, day);
    }

    @Override
    public Optional<String> answerForPart1() {
        loadBingoBoard();
        var score = findFirstBingoWinningScore();
        return fromNumber(score);
    }

    @Override
    public Optional<String> answerForPart2() {
        loadBingoBoard();
        var score = findLastBingoWinningScore();
        return fromNumber(score);
    }

    private long findFirstBingoWinningScore() {
        for (int bingoNumber : bingoNumbers) {
            for (BingoBoard bingoBoard : bingoBoards) {
                if (bingoBoard.mark(bingoNumber) && bingoBoard.bingo(bingoNumber)) {
                    return bingoBoard.computeScore(bingoNumber);
                }
            }
        }
        return -1;
    }


    private long findLastBingoWinningScore() {

        BingoBoard latestBingoWinner = null;
        int winningNumber = -1;

        LinkedList<BingoBoard> removalBoard = new LinkedList<>(bingoBoards);

        for (int bingoNumber : bingoNumbers) {

            if (removalBoard.isEmpty()) break;

            var iter =  removalBoard.iterator();
            while (iter.hasNext()) {
                BingoBoard bingoBoard = iter.next();
                if (bingoBoard.mark(bingoNumber) && bingoBoard.bingo(bingoNumber)) {
                    latestBingoWinner = bingoBoard;
                    winningNumber = bingoNumber;
                    iter.remove();
                }
            }
        }

        int finalWinningNumber = winningNumber;
        return Optional.ofNullable(latestBingoWinner).map(b -> b.computeScore(finalWinningNumber)).orElse(-1L);
    }

    void loadBingoBoard() {
        if (this.bingoBoards != null) return;

        List<String> lines = asList();
        var iterator = lines.iterator();
        String numbers = iterator.next();
        bingoNumbers = Arrays.stream(numbers.split(",")).mapToInt(Integer::parseInt).toArray();

        iterator.next();

        List<String> buffer = new ArrayList<>();
        List<BingoBoard> boards = new ArrayList<>();

        while (iterator.hasNext()) {
            var next = iterator.next();
            if (next.equals("")) {
                boards.add(createBingoBoard(buffer));
                buffer.clear();
            } else {
                buffer.add(next);
            }
        }

        boards.add(createBingoBoard(buffer));

        this.bingoBoards = boards;

    }

    BingoBoard createBingoBoard(List<String> lines) {

        int[][] board = lines.stream()
                .map(line -> Arrays.stream(line.split(" ")).filter(s -> !s.isEmpty())
                        .mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);

        return new BingoBoard(board);
    }

    private static class BingoBoard {

        private static final int SIZE = 5;

        private final HashMap<Integer, Point> bingoElementMap;
        private final int[][] board;
        private final boolean[][] markedBoard;
        private int marked = 0;


        public long computeScore(int bingoNumber) {

            long score = 0;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (!markedBoard[i][j]) {
                        score += board[i][j];
                    }
                }
            }

            return score * bingoNumber;
        }

        private static record Point(int rowIndex, int columnIndex) {
            void markBoard(boolean[][] board) {
                board[rowIndex][columnIndex] = true;
            }
        }

        private BingoBoard(int[][] board) {
            this.bingoElementMap = new HashMap<>();
            this.board = board;
            this.markedBoard = new boolean[SIZE][SIZE];
            for (int rowIndex = 0; rowIndex < SIZE; rowIndex++) {
                for (int columnIndex = 0; columnIndex < SIZE; columnIndex++) {
                    bingoElementMap.put(board[rowIndex][columnIndex], new Point(rowIndex, columnIndex));
                }
            }
        }

        public boolean mark(int bingoElement) {
            var point = Optional.ofNullable(bingoElementMap.get(bingoElement));
            point.ifPresent(p -> p.markBoard(markedBoard));
            if (point.isPresent()) marked++;
            return point.isPresent();
        }

        public boolean bingo(int bingoElement) {
            if (marked < SIZE) return false;

            var point = bingoElementMap.get(bingoElement);
            var row = point.rowIndex;

            boolean rowBingo = true;

            for (int i = 0; i < SIZE; i++) {
                if (!markedBoard[row][i]) {
                    rowBingo = false;
                    break;
                }
            }
            boolean columnBingo = true;
            var column = point.columnIndex;

            for (int j = 0; j < SIZE; j++) {
                if (!markedBoard[j][column]) {
                    columnBingo = false;
                    break;
                }
            }

            return rowBingo || columnBingo;
        }
    }

    public static void main(String[] args) {
        var solver = new Day4(AdventConstants.YEAR_2021, AdventConstants.DAY4);
        solver.run();
    }

}
