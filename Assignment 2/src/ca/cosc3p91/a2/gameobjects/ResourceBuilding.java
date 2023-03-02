package ca.cosc3p91.a2.gameobjects;

import ca.cosc3p91.a2.util.Time;

public abstract class ResourceBuilding extends Building {

    public static String resource;

    private final Time harvestMinTime = new Time().offsetSeconds(10);

    private int harvest_rate;
    private Time nextHarvestTime;

    public ResourceBuilding() {
        nextHarvestTime = Time.getTime().offsetTime(harvestMinTime);
    }

    public void upgrade(ResourceStage stage) {
        super.upgrade(stage);
        this.harvest_rate += stage.getHarvestRateIncrease();
    }

    public void update(Village_Hall hall){
        if (nextHarvestTime.occurred()){
            harvest(hall);
            nextHarvestTime = Time.getTime().offsetTime(harvestMinTime);
        }
    }

    protected abstract void harvest(Village_Hall hall);

    public int getHarvestRate(){
        return harvest_rate;
    }

}
