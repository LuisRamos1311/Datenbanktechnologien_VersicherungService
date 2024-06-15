package de.htwberlin.dao;

import de.htwberlin.domain.Ablehnungsregel;
import de.htwberlin.domain.Kunde;

import java.util.List;

public interface KundeDao {

    Kunde getKundeById(int kundeId);
}
