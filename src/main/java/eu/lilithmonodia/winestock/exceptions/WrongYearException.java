package eu.lilithmonodia.winestock.exceptions;

public class WrongYearException extends Exception{
    public WrongYearException() {
        super("The year of the bottle is incompatible with the assortment");
    }
}
