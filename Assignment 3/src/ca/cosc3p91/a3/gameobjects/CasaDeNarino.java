package ca.cosc3p91.a3.gameobjects;

public class CasaDeNarino extends Building {

    private int goldCapacity = 0;
    private int ironCapacity = 0;
    private int woodCapacity = 0;

    private int currentGold = 0;
    private int currentIron = 0;
    private int currentWood = 50;

    public CasaDeNarino(int lvl, VillageStage baseStage) {
        setLevel(lvl);
        upgrade(baseStage);
    }

    @Override
    public Stage getUpgradeStage() {
        return VillageHallStages.villageStages[getLevel()+1];
    }

    public void upgrade(VillageStage stage) {
        super.upgrade(stage);
        this.goldCapacity += stage.getGoldCapacityIncrease();
        this.ironCapacity += stage.getIronCapacityIncrease();
        this.woodCapacity += stage.getWoodCapacityIncrease();
    }


    public int getGoldCapacity() {
        return goldCapacity;
    }

    public int getIronCapacity() {
        return ironCapacity;
    }

    public int getWoodCapacity() {
        return woodCapacity;
    }

    public int getCurrentGold() {
        return currentGold;
    }

    public int getCurrentIron() {
        return currentIron;
    }

    public int getCurrentWood() {return currentWood;}

    public boolean addGold(int amount) {
        int newGold = this.currentGold + amount;
        if (newGold <= goldCapacity && this.currentGold + amount >= 0){
            this.currentGold += amount;
            return true;
        }
        return false;
    }

    public boolean addIron(int amount) {
        int newIron = this.currentIron + amount;
        if (newIron <= ironCapacity && newIron >= 0) {
            this.currentIron += amount;
            return true;
        }
        return false;
    }

    public boolean addWood(int amount) {
        int newWood = this.currentWood + amount;
        if (newWood <= woodCapacity && newWood >= 0) {
            this.currentWood += amount;
            return true;
        }
        return false;
    }
}
