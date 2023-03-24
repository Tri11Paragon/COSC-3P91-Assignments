package ca.cosc3p91.a3.gameobjects;

public class Farm extends ResourceBuilding {

    public Farm(int lvl, ResourceStage baseStage) {
        setLevel(lvl);
        upgrade(baseStage);
    }

    public int getPopulationContribution() {
        return getHarvestRate();
    }

    @Override
    protected ResourceHarvestHandler getHarvestHandler() {
        return hall -> {};
    }

    @Override
    public String getResource() {
        return "UNUSED";
    }

    @Override
    public Stage getUpgradeStage() {
        return ResourceStages.goldStages[getLevel()+1];
    }
}
