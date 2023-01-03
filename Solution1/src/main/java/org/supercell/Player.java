package org.supercell;

public class Player {
    String PlayerName;
    TimeMap timeMap;
    public Player(String PlayerName) {
        this.PlayerName = PlayerName;
        this.timeMap = new TimeMap();
    }
    public String toString() {
        return PlayerName;
    }
}
