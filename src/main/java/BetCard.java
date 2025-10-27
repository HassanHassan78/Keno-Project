/*
this class represents the card the player fills out with their number

    checks how many numbers the player can pick (1,,4,8,10)
    checks the actual numbers the player chose (1-80)
 */


import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class BetCard {
    private int spots = 0;
    private final LinkedHashSet<Integer> picks = new LinkedHashSet<>();

    /// sets how many spots the user is allowed to pick (1,4,8,10)
    public void  setSpots(int spots) {
        if (spots < 0) { spots = 0; }
        this.spots = spots;

        // if the user changes how many spots this trims the picks down
        while (picks.size() > spots) {
            Integer last = null;
            for (Integer i : picks) { last = i; }
            if (last != null) {picks.remove(last);}
            else {break;}
        }
    }

    /// add a number to the bet card return true if added false otherwise
    public boolean addPick(int pick) {
        if (spots <= 0) {return false;}
        if (pick <1 || pick >80) {return false;}
        if (picks.contains(pick)) {return false;}
        if (picks.size() >= spots) {return false;}
        return picks.add(pick);
    }

    /// remove a number if presnt, returns true if removed
    public boolean removePick(int pick) {
        return picks.remove(pick);
    }

    /// clears all chosen picks
    public void clear() {
        picks.clear();
    }

    /// true if the betcard has exactly spots picks selected
    public boolean isComplete() {
        return spots > 0 && picks.size() == spots;
    }

    /// getter  for spots
    public int getSpots() {
        return spots;
    }

    /// getter for picks that unchangeable outside this class
    public Set<Integer> getPicks() {
        return Collections.unmodifiableSet(picks);
    }
}
