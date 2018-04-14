package com.runapp.runapp_ma;

public class Route {

    private int id;
    private String title;
    private String description;
    private String departure;
    private float cost;
    private int spaces_available;

    public Route(int id, String title, String description, String departure, float cost, int spaces_available) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.departure = departure;
        this.cost = cost;
        this.spaces_available = spaces_available;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDate(){
        return departure.substring(5,7) + "/" + departure.substring(8,10);
    }

    public String getTime(){
        return departure.substring(11,16);
    }

    public String getCost() {
        return String.valueOf(cost);
    }

    public String getSpaces_available() {
        return String.valueOf(spaces_available);
    }
}
