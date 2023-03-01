package ca.cosc3p91.a2.gameobjects;

public abstract class ResourceBuidling extends Building {

    public static String resource;

    private int harvest_rate;

    public abstract void harvest(Village_Hall hall);

}
