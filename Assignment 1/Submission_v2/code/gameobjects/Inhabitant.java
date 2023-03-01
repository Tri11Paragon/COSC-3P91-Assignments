package gameobjects;

import game.Map;

public interface Inhabitant {

  public Map map = null;
  public Building building = null;

  public void move(Tile t);

  public void getPosition();

}
