import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class BetCardTests {

    private BetCard betCard;

    @BeforeEach
    void setUp() {
        betCard = new BetCard();
    }

	@Test
	void setSpotsAndGetSpotsTest() {
        betCard.setSpots(4);
        assertEquals(4, betCard.getSpots());
    }

    @Test
    void addPicksTest() {
        betCard.setSpots(4);
        assertTrue(betCard.addPick(10));
        assertTrue(betCard.getPicks().contains(10));
    }

    @Test
    void addPickDuplicateTest() {
        betCard.setSpots(4);
        assertTrue(betCard.addPick(10));
        assertFalse(betCard.addPick(10));
    }

    @Test
    void addPickOverLimitTest() {
        betCard.setSpots(2);
        assertTrue(betCard.addPick(10));
        assertTrue(betCard.addPick(11));
        assertFalse(betCard.addPick(12));
        assertEquals(2, betCard.getPicks().size());
    }

    @Test
    void removePicksTest() {
        betCard.setSpots(2);
        betCard.addPick(10);
        assertTrue(betCard.removePick(10));
        assertFalse(betCard.getPicks().contains(10));
    }

    @Test
    void clearPicksTest() {
        betCard.setSpots(2);
        betCard.addPick(10);
        betCard.addPick(11);
        betCard.clear();
        assertTrue(betCard.getPicks().isEmpty());
        assertEquals(2, betCard.getSpots());
    }

    @Test
    void isCompleteTrueTest() {
        betCard.setSpots(2);
        betCard.addPick(10);
        betCard.addPick(11);
        assertTrue(betCard.isComplete());
    }

    @Test
    void isCompleteFalseWhenUnderFilledTest() {
        betCard.setSpots(2);
        betCard.addPick(10);
        assertFalse(betCard.isComplete());
    }

    @Test
    void setSpotsTrimRemovesLastChosenFirstTest() {
        betCard.setSpots(4);
        betCard.addPick(10);
        betCard.addPick(11);
        betCard.addPick(12);
        betCard.addPick(13);
        betCard.setSpots(2);
        assertEquals(2, betCard.getPicks().size());
        assertTrue(betCard.getPicks().contains(10));
        assertTrue(betCard.getPicks().contains(11));
        assertFalse(betCard.getPicks().contains(12));
        assertFalse(betCard.getPicks().contains(13));
    }

    @Test
    void addPickOutOfRangeLowTest() {
        betCard.setSpots(1);
        assertFalse(betCard.addPick(0));
        assertTrue(betCard.getPicks().isEmpty());
    }

    @Test
    void addPickOutOfRangeHighTest() {
        betCard.setSpots(1);
        assertFalse(betCard.addPick(81));
        assertTrue(betCard.getPicks().isEmpty());
    }

    @Test
    void addBeforeSetingSpotsTest() {
        assertFalse(betCard.addPick(10));
        assertTrue(betCard.getPicks().isEmpty());
    }

    @Test
    void insertionOrderTest() {
        betCard.setSpots(4);
        betCard.addPick(10);
        betCard.addPick(1);
        betCard.addPick(12);
        List<Integer> order = new ArrayList<>(betCard.getPicks());
        assertEquals(List.of(10, 1, 12), order);
    }
}
