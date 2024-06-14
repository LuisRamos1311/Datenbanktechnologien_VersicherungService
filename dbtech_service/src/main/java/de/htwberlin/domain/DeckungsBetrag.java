package de.htwberlin.domain;

import java.math.BigDecimal;

public class DeckungsBetrag {

   private int ID;
   private int Deckungsart_FK;
   private BigDecimal Deckungsbetrag;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getDeckungsart_FK() {
        return Deckungsart_FK;
    }

    public void setDeckungsart_FK(int deckungsart_FK) {
        Deckungsart_FK = deckungsart_FK;
    }

    public BigDecimal getDeckungsbetrag() {
        return Deckungsbetrag;
    }

    public void setDeckungsbetrag(BigDecimal deckungsbetrag) {
        Deckungsbetrag = deckungsbetrag;
    }




}
