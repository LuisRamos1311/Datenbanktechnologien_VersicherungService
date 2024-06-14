package de.htwberlin.domain;

import java.math.BigDecimal;

public class Deckung {

    private int Vertrag_FK;
    private int Deckungsart_FK;
    private BigDecimal Deckungsbetrag;

    public BigDecimal getDeckungsbetrag() {
        return Deckungsbetrag;
    }

    public void setDeckungsbetrag(BigDecimal deckungsbetrag) {
        Deckungsbetrag = deckungsbetrag;
    }

    public int getDeckungsart_FK() {
        return Deckungsart_FK;
    }

    public void setDeckungsart_FK(int deckungsart_FK) {
        Deckungsart_FK = deckungsart_FK;
    }

    public int getVertrag_FK() {
        return Vertrag_FK;
    }

    public void setVertrag_FK(int vertrag_FK) {
        Vertrag_FK = vertrag_FK;
    }


}
