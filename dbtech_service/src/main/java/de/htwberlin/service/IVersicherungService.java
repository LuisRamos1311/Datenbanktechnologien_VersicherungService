package de.htwberlin.service;

/**
 * @author Ingo Classen
 */

import java.math.BigDecimal;
import java.sql.Connection;

import de.htwberlin.exceptions.DeckungsartExistiertNichtException;
import de.htwberlin.exceptions.DeckungsartNichtRegelkonformException;
import de.htwberlin.exceptions.DeckungsartPasstNichtZuProduktException;
import de.htwberlin.exceptions.DeckungspreisNichtVorhandenException;
import de.htwberlin.exceptions.UngueltigerDeckungsbetragException;
import de.htwberlin.exceptions.VertragExistiertNichtException;

public interface IVersicherungService {

  /**
   * Speichert die uebergebene Datenbankverbindung in einer Instanzvariablen.
   * 
   */
  void setConnection(Connection connection);

  /**
   * Fuegt eine Deckung einem Vertrag hinzu.
   * 
   * @param vertragsId     Fremdschluessel auf den Vertrag.
   * @param deckungsartId  Fremdschluessel auf die Deckungsart.
   * @param deckungsbetrag Betrag, der gedeckt werden soll.
   * @throws VertragExistiertNichtException          wenn vertragsId kein
   *                                                 gueltiger Primaerschluessel
   *                                                 fuer Vertraege ist.
   * @throws DeckungsartExistiertNichtException      wenn deckungsartId kein
   *                                                 gueltiger Primaerschluessel
   *                                                 fuer Deckungsarten ist.
   * @throws UngueltigerDeckungsbetragException      wenn der Deckungsbetrag fuer
   *                                                 die gegebene Deckung nicht
   *                                                 angeboten wird.
   * @throws DeckungsartPasstNichtZuProduktException wenn die Deckungsart nicht
   *                                                 fuer das Produkt des Vertrags
   *                                                 passt.
   * @throws DeckungsartNichtRegelkonformException   wenn eine Ablehnungsregel
   *                                                 zutrifft
   * @throws DeckungspreisNichtVorhandenException    wenn fuer einen
   *                                                 Deckungsbetrag kein Preis
   *                                                 vorliegt
   */
  void createDeckung(Integer vertragsId, Integer deckungsartId, BigDecimal deckungsbetrag);

}
