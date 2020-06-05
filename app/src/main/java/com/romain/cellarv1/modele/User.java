package com.romain.cellarv1.modele;

public class User {

    // Propriétés
    private Integer id;
    private String pseudo;
    private String password;
    private String mail;
    private String avatarlarge;
    private String avatarsmall;

    // Constructeur entier
    public User(Integer id, String pseudo, String password, String mail, String avatarlarge, String avatarsmall) {

        if (id != null ){
            this.id = id;
        } else {
            this.id = -1;
        }

        this.pseudo = pseudo;
        this.password = password;
        this.mail = mail;
        this.avatarlarge = avatarlarge;
        this.avatarsmall = avatarsmall;
    }

    // Getters et setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAvatarlarge() {
        return avatarlarge;
    }

    public void setAvatarlarge(String avatarlarge) {
        this.avatarlarge = avatarlarge;
    }

    public String getAvatarsmall() {
        return avatarsmall;
    }

    public void setAvatarsmall(String avatarsmall) {
        this.avatarsmall = avatarsmall;
    }
}
