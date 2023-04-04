package ca.cosc3p91.a4.gameobjects;

import ca.cosc3p91.a4.util.Time;

public class VillageHallStages {

    public static class VillageStage1 extends VillageStage {
        public VillageStage1() {
            super(100, 0, 0, new Time(), 0,
                    0, 1000, 2500, 5000);
        }
    }

    public static class VillageStage2 extends VillageStage {
        public VillageStage2() {
            super(550, 1000, 0, new Time().offsetHours(2), 2500,
                    5000, 2500, 5000, 10000);
        }
    }

    public static class VillageStage3 extends VillageStage {
        public VillageStage3() {
            super(550, 2500, 0, new Time().offsetHours(2), 5000,
                    10000, 5000, 7500, 15000);
        }
    }

    public static VillageStage[] villageStages = {new VillageStage1(), new VillageStage2(), new VillageStage3()};

}
