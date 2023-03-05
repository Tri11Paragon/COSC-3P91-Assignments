package ca.cosc3p91.a2.gameobjects;

import ca.cosc3p91.a2.util.Time;

public class DefenseStages {

    // !! need to adjust these values | |
    //                                v v

    public static class ArcherTowerStage1 extends DefenseStage {
        public ArcherTowerStage1() {
            super(100, 0, 0, new Time().offsetMinutes(1), 25, 250, 4,6);
        }
    }

    public static class ArcherTowerStage2 extends DefenseStage {
        public ArcherTowerStage2() {
            super(150, 0, 1, new Time().offsetMinutes(15), 25, 250, 8,8);
        }
    }

    public static class ArcherTowerStage3 extends DefenseStage {
        public ArcherTowerStage3() {
            super(200, 0, 2, new Time().offsetHours(1), 25, 250, 12,12);
        }
    }

    public static class CannonStage1 extends DefenseStage {
        public CannonStage1() {
            super(125, 0, 0, new Time().offsetMinutes(2), 25, 250, 8,4);
        }
    }

    public static class CannonStage2 extends DefenseStage {
        public CannonStage2() {
            super(175, 0, 1, new Time().offsetMinutes(20), 25, 250, 12,6);
        }
    }

    public static class CannonStage3 extends DefenseStage {
        public CannonStage3() {
            super(225, 0, 2, new Time().offsetHours(1), 25, 250, 14,8);
        }
    }

    public static DefenseStage[] archerTowerStages = {new DefenseStages.ArcherTowerStage1(), new DefenseStages.ArcherTowerStage2(), new DefenseStages.ArcherTowerStage3()};
    public static DefenseStage[] cannonStages = {new DefenseStages.CannonStage1(), new DefenseStages.CannonStage2(), new DefenseStages.CannonStage3()};

}
