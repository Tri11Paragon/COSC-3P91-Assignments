package ca.cosc3p91.a2.util;

import ChallengeDecision.*;
import ca.cosc3p91.a2.game.Map;
import ca.cosc3p91.a2.gameobjects.*;

import java.util.ArrayList;
import java.util.List;

public class ChallengeAdapter {

    private static class MapChallengeConverter {
        private final Map map;
        public MapChallengeConverter(Map map){
            this.map = map;
        }

        public List<ChallengeAttack<Double,Double>> getAttackers(){
            List<ChallengeAttack<Double,Double>> entityAttackList = new ArrayList<>();

            map.inhabitants.stream()
                    .filter(i -> i instanceof Infantry)
                    .map(i -> (Infantry) i)
                    .forEach(i -> {
                        entityAttackList.add(new ChallengeAttack<>((double) i.getDamage(), (double) i.getHealth()));
            });

            return entityAttackList;
        }

        public List<ChallengeDefense<Double,Double>> getDefenders(){
            List<ChallengeDefense<Double,Double>> entityDefenseList = new ArrayList<>();

            map.contains.stream()
                    .filter(b -> b instanceof DefenseBuilding)
                    .map(b -> (DefenseBuilding) b)
                    .forEach(d -> {
                entityDefenseList.add(new ChallengeDefense<>((double) d.getDamage(), (double) d.getHealth()));
            });

            return entityDefenseList;
        }

        public List<ChallengeResource<Double,Double>> getResources(String type){
            List<ChallengeResource<Double,Double>> entityResourceList = new ArrayList<>();

            CasaDeNarino th = map.getTownHall();

            int resourceCount;
            int thResourceCount =
                    type.equals(SaulGoodMine.resource) ? th.getCurrentGold()
                            : type.equals(IronMine.resource)
                                ? th.getCurrentIron() : th.getCurrentWood();

            resourceCount = (int) map.contains.stream()
                    .filter(b -> b instanceof ResourceBuilding).map(b -> (ResourceBuilding) b)
                    .filter(r -> r.getResource().equals(type))
                    .count();

            map.contains.stream()
                    .filter(b -> b instanceof ResourceBuilding)
                    .map(b -> (ResourceBuilding) b)
                    .filter(r -> r.getResource().equals(type))
                    .forEach((r) -> {
                        entityResourceList.add(
                                new ChallengeResource<>(
                                        (double) thResourceCount / (double) resourceCount,
                                        (double) r.getHealth())
                    );
            });

            return entityResourceList;
        }
    }

    private final Map map;

    public ChallengeAdapter(Map map){
        this.map = map;
    }

    public void attack(Map enemy){
        MapChallengeConverter enemyMap = new MapChallengeConverter(enemy);
        MapChallengeConverter ourMap = new MapChallengeConverter(this.map);

        List<ChallengeAttack<Double,Double>> ourAttackers = ourMap.getAttackers();
        // not needed
        List<ChallengeDefense<Double, Double>> ourDefenders = ourMap.getDefenders();

        ChallengeEntitySet<Double,Double> ourSet = new ChallengeEntitySet<>();
        ourSet.setEntityAttackList(ourAttackers);
        ourSet.setEntityDefenseList(ourDefenders);

        List<ChallengeDefense<Double,Double>> enemyDefenders = enemyMap.getDefenders();

        List<ChallengeResource<Double,Double>> enemyGold = enemyMap.getResources(SaulGoodMine.resource);
        List<ChallengeResource<Double,Double>> enemyIron = enemyMap.getResources(IronMine.resource);
        List<ChallengeResource<Double,Double>> enemyWood = enemyMap.getResources(LumberMine.resource);

        // split is required because challengeResource lacks any resource specifier
        ChallengeEntitySet<Double,Double> enemyGoldSet = new ChallengeEntitySet<>();
        ChallengeEntitySet<Double,Double> enemyIronSet = new ChallengeEntitySet<>();
        ChallengeEntitySet<Double,Double> enemyWoodSet = new ChallengeEntitySet<>();

        enemyGoldSet.setEntityDefenseList(enemyDefenders);
        enemyIronSet.setEntityDefenseList(enemyDefenders);
        enemyWoodSet.setEntityDefenseList(enemyDefenders);

        enemyGoldSet.setEntityResourceList(enemyGold);
        enemyIronSet.setEntityResourceList(enemyIron);
        enemyWoodSet.setEntityResourceList(enemyWood);

        ChallengeResult goldResults = Arbitrer.challengeDecide(ourSet, enemyGoldSet);
        ChallengeResult ironResults = Arbitrer.challengeDecide(ourSet, enemyIronSet);
        ChallengeResult woodResults = Arbitrer.challengeDecide(ourSet, enemyWoodSet);

        // if any fail to attack we need to pretend like it was one big attack that failed
        if (!goldResults.getChallengeWon() || !ironResults.getChallengeWon() || !woodResults.getChallengeWon())
            return;

        System.out.println("We won gold: ");
        goldResults.print();
        System.out.println("We won iron: ");
        ironResults.print();
        System.out.println("We won wood: ");
        woodResults.print();

        CasaDeNarino th = map.getTownHall();

        goldResults.getLoot().forEach(r -> {
            th.addGold((int)r.getProperty().doubleValue());
        });

        ironResults.getLoot().forEach(r -> {
            th.addIron((int)r.getProperty().doubleValue());
        });

        woodResults.getLoot().forEach(r -> {
            th.addWood((int)r.getProperty().doubleValue());
        });
    }

}
