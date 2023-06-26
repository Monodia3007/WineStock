package eu.lilithmonodia.winestock.app;

import java.time.Year;

public class Wine {
    private String name;
    private Year year;
    private int volume;
    private int price;
    private String comment;

    public Wine(String name, Year year, int volume, int price) {
        this(name, year, volume, price, "");
    }

    public Wine(String name, Year year, int volume, int price, String comment) {
        this.name = name;
        this.year = year;
        this.volume = volume;
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

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getPrice() {
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
        if (o == null || getClass() != o.getClass()) return false;

        Wine wine = (Wine) o;

        if (getVolume() != wine.getVolume()) return false;
        if (getPrice() != wine.getPrice()) return false;
        if (!getName().equals(wine.getName())) return false;
        return getYear().equals(wine.getYear());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getYear().hashCode();
        result = 31 * result + getVolume();
        result = 31 * result + getPrice();
        return result;
    }

    @Override
    public String toString() {
        return "Wine{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", volume=" + volume +
                ", price=" + price +
                ", comment='" + comment + '\'' +
                '}';
    }
}
