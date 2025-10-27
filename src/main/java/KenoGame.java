/*
this class will store the game state and run all the logic of the game
        draws 20 numbers
        counts matchs against BetCard
        looks up payouts in the payout table
 */

import java.util.*;

public class KenoGame {
    private final Random rand = new Random();
    private final BetCard bc = new BetCard();
    private final PayoutTable pt = new PayoutTable();

    private int drawingsToPlay = 0;
    private int drawingsPlayed = 0;
    private long totalWon = 0;
    private boolean drawing = false;
    private List<Integer> lastMatched = new ArrayList<>();

    /// set a new round with N drawings
    /// resets totals and state
    public void startRound(int drawings) {
        this.drawingsToPlay = Math.max(0, drawings);
        this.drawingsPlayed = 0;
        this.totalWon = 0;
        this.lastMatched.clear();
        this.drawing = drawingsToPlay > 0;
    }

    /// generate 20 unique numbers from 1-80
    public List<Integer> draw20() {
        List<Integer> pool = new ArrayList<>(80);
        for (int i = 0; i < 80; i++) {pool.add(i);}
        Collections.shuffle(pool, rand);
        return new ArrayList<>(pool.subList(0, 20));
    }

    /// counts matches vs current betcard
    public int countMatches(List<Integer> draw) {
        Set<Integer> set = new HashSet<>(draw);
        lastMatched = new ArrayList<>();
        for (int i : bc.getPicks()) {
            if (set.contains(i)) {lastMatched.add(i);}
        }
        return lastMatched.size();
    }

    /// payout dollars for curr round
    public long payout(int spots, int matches) {
        return pt.compute(spots, matches);
    }

    /// add to you current total
    public void addWinnings(long winnings) {
        totalWon += Math.max(0, winnings);
    }

    /// mark one drawing finished
    /// stop the round if none remain
    public void finishedDrawing() {
        drawingsPlayed++;
        if (drawingsPlayed >= drawingsToPlay) {drawing = false;}
    }

    //getters for UI

    public Random getRand() {
        return rand;
    }
    public BetCard getBc() {
        return bc;
    }
    public boolean hasNextDrawing() {
        return drawingsPlayed < drawingsToPlay;
    }
    public int getDrawingsToPlay() {
        return drawingsToPlay;
    }
    public int getDrawingsPlayed() {
        return drawingsPlayed;
    }
    public long getTotalWon() {
        return totalWon;
    }
    public boolean isDrawing() {
        return drawing;
    }
    public List<Integer> getLastMatched() {
        return lastMatched;
    }
}
