package ca.cosc3p91.a2.gameobjects;

public class GoldMine extends ResourceBuilding {

  public static String resource = "gold";

  public GoldMine (int lvl, ResourceStage baseStage) {
    setLevel(lvl);
    upgrade(baseStage);
  }

  @Override
  public void harvest(Village_Hall hall) {

  }
}
