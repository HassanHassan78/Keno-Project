/*
a helper class that calculates the money the player has won
 */

import java.util.HashMap;
import java.util.Map;

public class PayoutTable {
    private final Map<Integer, int[]> table = new HashMap<>();

    public  PayoutTable() {
        table.put(1, new int[]{0, 2});
        table.put(4, new int[]{0, 0, 1, 5, 75});
        table.put(8, new int[]{0, 0, 0, 0, 2, 12, 50, 750, 10_000});
        table.put(10, new int[]{5, 0, 0, 0, 0, 2, 15, 40, 450, 4_250, 100_000});
    }

    public long compute(int spots, int matches) {
        int[] arr = table.get(spots);
        if (arr == null) { return 0; }
        if (matches < 0 || matches >= arr.length) { return 0; }
        return arr[matches];
    }
}
