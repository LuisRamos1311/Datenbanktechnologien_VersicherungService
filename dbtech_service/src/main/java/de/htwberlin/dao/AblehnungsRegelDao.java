package de.htwberlin.dao;

import de.htwberlin.domain.Ablehnungsregel;

import java.util.List;

public interface AblehnungsRegelDao {

    Ablehnungsregel getAblehnungsRegelByDeckungsArtId(int deckungsart_FK);

    List<Ablehnungsregel> getAblehnungsRegelListByDeckungsArtId(int deckungsArtId);

}
