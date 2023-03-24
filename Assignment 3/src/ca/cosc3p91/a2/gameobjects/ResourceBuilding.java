package ca.cosc3p91.a2.gameobjects;

import ca.cosc3p91.a2.util.Time;

public abstract class ResourceBuilding extends Building {

    protected interface ResourceHarvestHandler {
        void harvest(CasaDeNarino hall);
    }

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

    public void update(CasaDeNarino hall){
        if (nextHarvestTime.occurred()){
            getHarvestHandler().harvest(hall);
            nextHarvestTime = Time.getTime().offsetTime(harvestMinTime);
        }
    }

    protected abstract ResourceHarvestHandler getHarvestHandler();

    public int getHarvestRate(){
        return harvest_rate;
    }

    public abstract String getResource();

}
