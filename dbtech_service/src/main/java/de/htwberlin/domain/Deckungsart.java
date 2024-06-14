package de.htwberlin.domain;

public class Deckungsart {

    private int ID;
    private int Produkt_FK;
    private String KurzBez;
    private String Bez;

    public int getProdukt_FK() {
        return Produkt_FK;
    }

    public void setProdukt_FK(int produkt_FK) {
        Produkt_FK = produkt_FK;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getKurzBez() {
        return KurzBez;
    }

    public void setKurzBez(String kurzBez) {
        KurzBez = kurzBez;
    }

    public String getBez() {
        return Bez;
    }

    public void setBez(String bez) {
        Bez = bez;
    }

}
