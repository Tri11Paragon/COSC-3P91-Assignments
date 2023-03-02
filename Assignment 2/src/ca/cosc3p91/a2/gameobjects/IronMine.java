package ca.cosc3p91.a2.gameobjects;

public class IronMine extends ResourceBuilding {

    public static String resource = "iron";

    public IronMine(int lvl, ResourceStage baseStage) {
        setLevel(lvl);
        upgrade(baseStage);
    }

    @Override
    public void harvest(Village_Hall hall) {
        hall.addCurrentIron(getHarvestRate());
    }
}
