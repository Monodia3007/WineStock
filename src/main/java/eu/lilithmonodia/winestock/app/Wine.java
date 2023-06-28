package eu.lilithmonodia.winestock.app;

import java.time.Year;

public class Wine {
    private String name;
    private Year year;
    private BottleSize volume;
    private Color color;
    private double price;
    private String comment;

    public Wine(String name, int year, double volume, String color, double price) {
        this(name, year, volume, color, price, "");
    }

    public Wine(String name, int year, double volume, String color, double price, String comment) {
        this.name = name;
        this.year = Year.of(year);
        this.volume = BottleSize.doubleToBottleSize(volume);
        this.color = Color.valueOf(color.toUpperCase());
        this.price = price;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public BottleSize getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = BottleSize.doubleToBottleSize(volume);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wine wine)) return false;

        if (Double.compare(wine.getPrice(), getPrice()) != 0) return false;
        if (!getName().equals(wine.getName())) return false;
        if (!getYear().equals(wine.getYear())) return false;
        if (getVolume() != wine.getVolume()) return false;
        return getColor() == wine.getColor();
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getName().hashCode();
        result = 31 * result + getYear().hashCode();
        result = 31 * result + getVolume().hashCode();
        result = 31 * result + getColor().hashCode();
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Wine{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", volume=" + volume.toString() + '(' + volume.getVolume() + ')' +
                ", color=" + color +
                ", price=" + price +
                ", comment='" + comment + '\'' +
                '}';
    }
}
