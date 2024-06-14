package de.htwberlin.domain;

import java.time.LocalDate;

public class Vertrag {
    private int ID = 0;
    private int PRODUKT_FK;
    private int KUNDE_FK;
    private LocalDate VERSICHERUNGSBEGINN;
    private LocalDate VERSICHERUNGSENDE;

    public Vertrag() {
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int iD) {
        this.ID = iD;
    }

    public int getPRODUKT_FK() {
        return this.PRODUKT_FK;
    }

    public void setPRODUKT_FK(int pRODUKT_FK) {
        this.PRODUKT_FK = pRODUKT_FK;
    }

    public int getKUNDE_FK() {
        return this.KUNDE_FK;
    }

    public void setKUNDE_FK(int kUNDE_FK) {
        this.KUNDE_FK = kUNDE_FK;
    }

    public LocalDate getVERSICHERUNGSBEGINN() {
        return this.VERSICHERUNGSBEGINN;
    }

    public void setVERSICHERUNGSBEGINN(LocalDate vERSICHERUNGSBEGINN) {
        this.VERSICHERUNGSBEGINN = vERSICHERUNGSBEGINN;
    }

    public LocalDate getVERSICHERUNGSENDE() {
        return this.VERSICHERUNGSENDE;
    }

    public void setVERSICHERUNGSENDE(LocalDate vERSICHERUNGSENDE) {
        this.VERSICHERUNGSENDE = vERSICHERUNGSENDE;
    }
}