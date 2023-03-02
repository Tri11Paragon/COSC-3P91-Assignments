package ca.cosc3p91.a2.gameobjects;

public abstract class ResourceBuilding extends Building {

    public static String resource;
    private int harvest_rate;

    public void upgrade(ResourceStage stage) {
        super.upgrade(stage);
        this.harvest_rate += stage.getHarvestRateIncrease();
    }

    public abstract void harvest(Village_Hall hall);

}
