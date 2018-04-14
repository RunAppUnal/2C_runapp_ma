package com.runapp.runapp_ma;

public class Vehicle {

    private int id;
    private String plate;
    private int user_id;
    private String kind;
    private int model;
    private String color;
    private int capacity;
    private String image;
    private String brand;

    public Vehicle(int id, String plate, int user_id, String kind, int model, String color, int capacity, String image, String brand){
        this.id = id;
        this.plate = plate;
        this.user_id = user_id;
        this.kind = kind;
        this.model = model;
        this.color = color;
        this.capacity = capacity;
        this.image = image;
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public String getPlate() {
        return plate;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getKind() {
        return kind;
    }

    public int getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getImage() {
        return image;
    }

    public String getBrand() {
        return brand;
    }
}
