package de.htwberlin.dao;

import java.math.BigDecimal;

public interface DeckungsDao {

    void createDeckung(int vertragId, int deckungsArt, BigDecimal deckungsBetragId);

}
