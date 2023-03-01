package gameobjects;

import String;

public abstract class ResourceBuidling extends Building {

  public static String resource;

  private int harvest_rate;

  public abstract void harvest(Village_Hall hall);

}
