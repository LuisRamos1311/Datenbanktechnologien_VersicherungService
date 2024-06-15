package de.htwberlin.dao;

import de.htwberlin.domain.DeckungsPreis;

import java.util.List;

public interface DeckungsPreisDao {

    DeckungsPreis getDeckungsPreisByDeckungsbetrag_FK(int deckungsBetragId);

    List<DeckungsPreis> getDeckungsPreisListByDeckungsbetrag_FK(int deckungsBetragId);

}
