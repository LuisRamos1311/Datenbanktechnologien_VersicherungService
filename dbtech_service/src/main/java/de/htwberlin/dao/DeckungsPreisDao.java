package de.htwberlin.dao;

import de.htwberlin.domain.DeckungsPreis;

public interface DeckungsPreisDao {

    DeckungsPreis getDeckungsPreisByDeckungsbetrag_FK(int deckungsBetragId);

}
