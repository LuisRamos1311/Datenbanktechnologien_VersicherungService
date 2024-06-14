package de.htwberlin.domain;

import java.time.LocalDate;

public class Ablehnungsregel {

    private int Deckungsart_FK;
    private int LfdNr;
    private String R_Betrag;
    private String R_Alter;


    public int getLfdNr() {
        return LfdNr;
    }

    public void setLfdNr(int lfdNr) {
        LfdNr = lfdNr;
    }

    public int getDeckungsart_FK() {
        return Deckungsart_FK;
    }

    public void setDeckungsart_FK(int deckungsart_FK) {
        Deckungsart_FK = deckungsart_FK;
    }

    public String getR_Betrag() {
        return R_Betrag;
    }

    public void setR_Betrag(String r_Betrag) {
        R_Betrag = r_Betrag;
    }

    public String getR_Alter() {
        return R_Alter;
    }

    public void setR_Alter(String r_Alter) {
        R_Alter = r_Alter;
    }
}
