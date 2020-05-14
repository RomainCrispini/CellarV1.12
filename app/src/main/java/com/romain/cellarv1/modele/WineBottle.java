package com.romain.cellarv1.modele;

import java.io.Serializable;
import java.util.Date;


public class WineBottle implements Serializable {

    // Propriétés
    private Date dateAddNewBottle;
    private String country;
    private String region;
    private String wineColor;
    private String domain;
    private String appellation;
    private Integer year;
    private Integer apogee;
    private Integer number;
    private Integer estimate;
    private String image;
    private String favorite;
    private String wish;
    private String random;


    // Constructeur
    public WineBottle(Date dateAddNewBottle, String country, String region, String wineColor, String domain, String appellation, Integer year, Integer apogee, Integer number, Integer estimate, String image, String favorite, String wish, String random) {
        this.dateAddNewBottle = dateAddNewBottle;
        this.country = country;
        this.region = region;
        this.wineColor = wineColor;
        this.domain = domain;
        this.appellation = appellation;
        this.year = year;
        this.apogee = apogee;
        this.number = number;
        this.estimate = estimate;
        this.image = image;
        this.favorite = favorite;
        this.wish = wish;
        this.random = random;
    }

    // Getters et setters
    public Date getDateAddNewBottle() {
        return dateAddNewBottle;
    }

    public void setDateAddNewBottle(Date dateAddNewBottle) {
        this.dateAddNewBottle = dateAddNewBottle;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getWineColor() {
        return wineColor;
    }

    public void setWineColor(String wineColor) {
        this.wineColor = wineColor;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getApogee() {
        return apogee;
    }

    public void setApogee(Integer apogee) {
        this.apogee = apogee;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getEstimate() {
        return estimate;
    }

    public void setEstimate(Integer estimate) {
        this.estimate = estimate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    @Override
    public String toString() {
        return "Pays : " + country + ", Région : " + region + ", Couleur : " + wineColor + image + "\n";
    }

    /*
    @Override
    public String toString() {
        return "WineBottle{" +
                "dateMesure=" + dateAddNewBottle +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", wineColor=" + wineColor +
                ", domain='" + domain + '\'' +
                ", appellation='" + appellation + '\'' +
                ", year=" + year +
                ", number=" + number +
                ", estimate=" + estimate +
                ", image='" + image + '\'' +
                '}';
    }*/
}
