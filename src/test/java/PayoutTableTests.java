import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PayoutTableTests {
    private static PayoutTable pt;

    @BeforeAll
    static void init() {
        pt = new PayoutTable();
    }

    @Test
    void computeUnkownSpotsTests() {
        assertEquals(0, pt.compute(2, 1));
        assertEquals(0, pt.compute(7, 3));
    }
    @Test
    void computeOutOfRangeTests() {
        assertEquals(0, pt.compute(1, -1));
        assertEquals(0, pt.compute(1, 100));
    }
    @Test
    void tablesExistsTests() {
        for (int spots : new int[]{1,4,8,10}) {
            for (int i=0; i<=10; i++) {
                assertTrue(pt.compute(spots, i) >=0);
            }
        }
    }
}
