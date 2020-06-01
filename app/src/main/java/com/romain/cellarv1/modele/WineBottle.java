package com.romain.cellarv1.modele;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;


public class WineBottle implements Serializable {

    // Propriétés
    private Integer id;
    private String country;
    private String region;
    private String wineColor;
    private String domain;
    private String appellation;
    private Integer year;
    private Integer apogee;
    private Integer number;
    private Integer estimate;
    private String pictureLarge;
    private String pictureSmall;
    private byte[] imageLarge;
    private byte[] imageSmall;
    private Float rate;
    private String favorite;
    private String wish;
    private Float lattitude;
    private Float longitude;
    private String timeStamp;



    // Constructeur total
    public WineBottle(Integer id, String country, String region, String wineColor, String domain, String appellation, Integer year, Integer apogee, Integer number, Integer estimate, String pictureLarge, String pictureSmall, byte[] imageLarge, byte[] imageSmall, Float rate, String favorite, String wish, Float lattitude, Float longitude, String timeStamp) {

        if (id != null ){
            this.id = id;
        } else {
            this.id = -1;
        }

        this.country = country;
        this.region = region;
        this.wineColor = wineColor;
        this.domain = domain;
        this.appellation = appellation;
        this.year = year;
        this.apogee = apogee;
        this.number = number;
        this.estimate = estimate;
        this.pictureLarge = pictureLarge;
        this.pictureSmall = pictureSmall;
        this.imageLarge = imageLarge;
        this.imageSmall = imageSmall;
        this.rate = rate;
        this.favorite = favorite;
        this.wish = wish;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.timeStamp = timeStamp;
    }

    // Constructeur pour pouvoir faire la somme estimate & number group by year
    public WineBottle(Integer id, Integer year, Integer number, Integer estimate) {
        this.id = id;
        this.year = year;
        this.number = number;
        this.estimate = estimate;
    }

    // Getters et setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPictureLarge() {
        return pictureLarge;
    }

    public void setPictureLarge(String pictureLarge) {
        this.pictureLarge = pictureLarge;
    }

    public String getPictureSmall() {
        return pictureSmall;
    }

    public void setPictureSmall(String pictureSmall) {
        this.pictureSmall = pictureSmall;
    }

    public byte[] getImageLarge() {
        return imageLarge;
    }

    public void setImageLarge(byte[] imageLarge) {
        this.imageLarge = imageLarge;
    }

    public byte[] getImageSmall() {
        return imageSmall;
    }

    public void setImageSmall(byte[] imageSmall) {
        this.imageSmall = imageSmall;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
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

    public Float getLattitude() {
        return lattitude;
    }

    public void setLattitude(Float lattitude) {
        this.lattitude = lattitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }


    @Override
    public String toString() {
        return "Pays : " + country + ", Région : " + region + ", Couleur : " + wineColor + pictureLarge + "\n";
    }

}
