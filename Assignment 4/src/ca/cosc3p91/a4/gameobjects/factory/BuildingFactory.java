package ca.cosc3p91.a4.gameobjects.factory;

import ca.cosc3p91.a4.gameobjects.*;
import ca.cosc3p91.a4.util.Util;

public class BuildingFactory {
    public Building getBuilding(String buildingName) {
        buildingName = buildingName.toLowerCase();
        char c = Util.getFirstChar(buildingName);

        if (buildingName.contains("gold") || buildingName.contains("good") || c == 'g') {
            return new SaulGoodMine(ResourceStages.goldStages[0]);
        } else if (buildingName.contains("iron") || c == 'i') {
            return new IronMine(ResourceStages.ironStages[0]);
        } else if (buildingName.contains("wood") || buildingName.contains("lumber") || c == 'w' || c == 'l') {
            return new LumberMine(ResourceStages.woodStages[0]);
        } else if (buildingName.contains("archer") || c == 'a') {
            return new ArcherTower();
        } else if (buildingName.contains("can") || c == 'c') {
            return new Cannon();
        }

        return null;
    }
}
