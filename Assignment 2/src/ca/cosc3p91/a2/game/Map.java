package ca.cosc3p91.a2.game;

import ca.cosc3p91.a2.gameobjects.*;

import java.util.ArrayList;
import java.util.List;

public class Map {

    static int MAXSIZE = 50;

    private CasaDeNarino townHall;

    private int guardTime;

    public List<Building> contains;

    public List<Inhabitant> inhabitants;

    public Map(CasaDeNarino casaDeNarino, int gTime) {
        contains = new ArrayList<>();
        inhabitants = new ArrayList<>();
        this.townHall = casaDeNarino;
        this.contains.add(casaDeNarino);
        this.inhabitants.add(new Worker()); this.inhabitants.add(new Worker());
        this.inhabitants.add(new Collector());
        this.guardTime = gTime;
    }

    public void move(Infantry i, Tile t) {

    }

    public void inRange(Infantry i, Building b) {

    }

    public boolean build(Tile t, Building b) {
        int goldCost = b.getStage().getCost(SaulGoodMine.resource);
        int ironCost = b.getStage().getCost(IronMine.resource);
        int woodCost = b.getStage().getCost(LumberMine.resource);
        CasaDeNarino hall = getTownHall();
        if (hall.getCurrentGold() >= goldCost && hall.getCurrentIron() >= ironCost && hall.getCurrentWood() >= woodCost) {
            if(!hall.addGold(-goldCost))
                throw new RuntimeException("Unable to subtract gold despite valid check!");
            if(!hall.addIron(-ironCost))
                throw new RuntimeException("Unable to subtract iron despite valid check!");
            if(!hall.addWood(-woodCost))
                throw new RuntimeException("Unable to subtract wood despite valid check!");
            contains.add(b);
            return true;
        } else
            return false;
    }

    public boolean train(Inhabitant i) {
        CasaDeNarino hall = getTownHall();
        int goldCost = i.getCost();

        if (hall.getCurrentGold() >= goldCost) {
            if(!hall.addGold(-goldCost))
                throw new RuntimeException("Unable to subtract gold despite valid check!");
            inhabitants.add(i);
            return true;
        } else return false;
    }

    public int getGuardTime() {
        return guardTime;
    }

    public void setGuardTime(int gTime) {
        this.guardTime = gTime;
    }

    public CasaDeNarino getTownHall(){
        return townHall;
    }

}
