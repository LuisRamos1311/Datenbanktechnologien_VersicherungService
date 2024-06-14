package de.htwberlin.domain;

public class Produkt {

    private int ID;
    private String KurzBez;
    private String Bez;


    public String getKurzBez() {
        return KurzBez;
    }

    public void setKurzBez(String kurzBez) {
        KurzBez = kurzBez;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBez() {
        return Bez;
    }

    public void setBez(String bez) {
        Bez = bez;
    }
    }
