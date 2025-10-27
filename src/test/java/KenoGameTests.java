import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KenoGameTests {
    private KenoGame g;

    @BeforeEach
    public void setUp() {
        g = new KenoGame();
    }

    @Test
    void draw20HasSize20Test() {
        List<Integer> draw = g.draw20();
        assertEquals(20, draw.size());
    }

    @Test
    void draw20HasNoDupTest() {
        List<Integer> draw = g.draw20();
        assertEquals(20, new HashSet<>(draw).size());
    }

    @Test
    void draw20InRangeTest() {
        List<Integer> draw = g.draw20();
        assertTrue(draw.stream().allMatch(x -> x >=1 && x <=80));
    }

    @Test
    void countMatchesZeroTest() {
        g.getBc().setSpots(4);
        g.getBc().addPick(1);
        g.getBc().addPick(2);
        g.getBc().addPick(3);
        g.getBc().addPick(4);

        long m = g.countMatches(Arrays.asList(21,22,23,24,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20));
        assertEquals(0, m);
        assertTrue(g.getLastMatched().isEmpty());
    }

    @Test
    void countMatchesAndLastMatchedTest() {
        g.getBc().setSpots(4);
        g.getBc().addPick(1);
        g.getBc().addPick(2);
        g.getBc().addPick(3);
        g.getBc().addPick(4);

        List<Integer> draw = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
        long m = g.countMatches(draw);
        assertEquals(4, m);
        assertEquals(List.of(1,2,3,4), g.getLastMatched());
    }

    @Test
    void StartRoundTest() {
        g.addWinnings(123);
        g.startRound(3);
        assertEquals(0, g.getDrawingsPlayed());
        assertEquals(3, g.getDrawingsToPlay());
        assertEquals(0, g.getTotalWon());
        assertTrue(g.isDrawing());
    }

    @Test
    void hasNextDrawingTest() {
        g.startRound(2);
        assertTrue(g.hasNextDrawing());
        g.finishedDrawing();
        assertTrue(g.hasNextDrawing());
        g.finishedDrawing();
        assertFalse(g.hasNextDrawing());
        assertFalse(g.isDrawing());
    }

    @Test
    void payoutTest() {
        for (int i : List.of(1,2,3,4)) {
            for (int j = 0; j <= 10; j++) {
                assertTrue(g.payout(i, j) >= 0);
            }
        }
    }
}
