package de.htwberlin.domain;

import java.time.LocalDate;

public class DeckungsPreis {

    private int ID;
    private int Deckungsbetrag_FK;
    private LocalDate Gueltig_Von;
    private LocalDate Gueltig_Bis;
    private int Preis;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getDeckungsbetrag_FK() {
        return Deckungsbetrag_FK;
    }

    public void setDeckungsbetrag_FK(int deckungsbetrag_FK) {
        Deckungsbetrag_FK = deckungsbetrag_FK;
    }

    public LocalDate getGueltig_Von() {
        return Gueltig_Von;
    }

    public void setGueltig_Von(LocalDate gueltig_Von) {
        Gueltig_Von = gueltig_Von;
    }

    public LocalDate getGueltig_Bis() {
        return Gueltig_Bis;
    }

    public void setGueltig_Bis(LocalDate gueltig_Bis) {
        Gueltig_Bis = gueltig_Bis;
    }

    public int getPreis() {
        return Preis;
    }

    public void setPreis(int preis) {
        Preis = preis;
    }
}
