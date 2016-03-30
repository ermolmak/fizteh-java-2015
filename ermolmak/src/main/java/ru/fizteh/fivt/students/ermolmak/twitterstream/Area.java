package ru.fizteh.fivt.students.ermolmak.twitterstream;

import twitter4j.GeoLocation;

class Area {
    private GeoLocation location;
    private double radius;

    Area(GeoLocation location, double radius) {
        this.location = location;
        this.radius = radius;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
