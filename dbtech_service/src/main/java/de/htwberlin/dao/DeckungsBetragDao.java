package de.htwberlin.dao;

import de.htwberlin.domain.DeckungsBetrag;

import java.math.BigDecimal;

public interface DeckungsBetragDao {

    DeckungsBetrag getDeckungsBetragByDeckungsart_FKAndDeckungsBetrag(int deckungsBetragById, BigDecimal deckungsBetrag);

}
