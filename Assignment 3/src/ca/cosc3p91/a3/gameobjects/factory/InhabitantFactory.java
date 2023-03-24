package ca.cosc3p91.a3.gameobjects.factory;

import ca.cosc3p91.a3.gameobjects.*;
import ca.cosc3p91.a3.util.Util;

public class InhabitantFactory {
    public Inhabitant getInhabitant(String inhabitantName) {
        inhabitantName = inhabitantName.toLowerCase();
        char c = Util.getFirstChar(inhabitantName);

        if (inhabitantName.contains("soldier") || c == 's') {
            return new Soldier();
        } else if (inhabitantName.contains("knight") || c == 'k') {
            return new Knight();
        } else if (inhabitantName.contains("work") || c == 'w') {
            return new Worker();
        } else if (inhabitantName.contains("collect") || c == 'c') {
            return new Collector();
        } else if (inhabitantName.contains("cat")) {
            return new Catapult();
        } else if (inhabitantName.contains("arch") || c == 'a') {
            return new Archer();
        }

        return null;
    }
}
