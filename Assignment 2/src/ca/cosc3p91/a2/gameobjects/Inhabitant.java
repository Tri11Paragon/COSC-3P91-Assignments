package ca.cosc3p91.a2.gameobjects;

import ca.cosc3p91.a2.game.Map;

public interface Inhabitant {

  public Map map = null;
  public Building building = null;

  public void move(Tile t);

  public void getPosition();

}
