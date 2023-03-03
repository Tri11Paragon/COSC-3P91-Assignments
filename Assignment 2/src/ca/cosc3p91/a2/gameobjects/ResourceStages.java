package ca.cosc3p91.a2.gameobjects;

import ca.cosc3p91.a2.util.Time;

public class ResourceStages {

    public static class GoldStage1 extends ResourceStage {
        public GoldStage1() {
            super(100, 0, 0, new Time().offsetMinutes(1), 25, 250, 25);
        }
    }

    public static class GoldStage2 extends ResourceStage {
        public GoldStage2() {
            super(100, 100, 1, new Time().offsetMinutes(15), 25, 275, 35);
        }
    }

    public static class GoldStage3 extends ResourceStage {
        public GoldStage3() {
            super(100, 150, 2, new Time().offsetHours(1), 50, 325, 50);
        }
    }

    public static class IronStage1 extends ResourceStage {
        public IronStage1() {
            super(100, 0, 0, new Time().offsetMinutes(1), 0, 125, 25);
        }
    }

    public static class IronStage2 extends ResourceStage {
        public IronStage2() {
            super(100, 15, 1, new Time().offsetMinutes(15), 25, 155, 35);
        }
    }

    public static class IronStage3 extends ResourceStage {
        public IronStage3() {
            super(100, 50, 2, new Time().offsetHours(1), 50, 250, 50);
        }
    }

    public static class WoodStage1 extends ResourceStage {
        public WoodStage1() {
            super(100, 0, 0, new Time().offsetMinutes(1), 0, 50, 25);
        }
    }

    public static class WoodStage2 extends ResourceStage {
        public WoodStage2() {
            super(100, 5, 1, new Time().offsetMinutes(15), 5, 75, 35);
        }
    }

    public static class WoodStage3 extends ResourceStage {
        public WoodStage3() {
            super(100, 10, 2, new Time().offsetHours(1), 25, 100, 50);
        }
    }

    public static ResourceStage[] goldStages = {new GoldStage1(), new GoldStage2(), new GoldStage3()};
    public static ResourceStage[] ironStages = {new IronStage1(), new IronStage2(), new IronStage3()};
    public static ResourceStage[] woodStages = {new WoodStage1(), new WoodStage2(), new WoodStage3()};

}
