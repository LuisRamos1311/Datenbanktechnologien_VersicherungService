package de.htwberlin.service;

/**
 * @author Ingo Classen
 */

import java.math.BigDecimal;
import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.exceptions.DataException;

/**
 * VersicherungJdbc
 */
public class VersicherungService implements IVersicherungService {
  private static final Logger L = LoggerFactory.getLogger(VersicherungService.class);
  private Connection connection;

  @Override
  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  @SuppressWarnings("unused")
  private Connection useConnection() {
    if (connection == null) {
      throw new DataException("Connection not set");
    }
    return connection;
  }

  @Override
  public void createDeckung(Integer vertragsId, Integer deckungsartId, BigDecimal deckungsbetrag) {
    L.info("vertragsId: " + vertragsId);
    L.info("deckungsartId: " + deckungsartId);
    L.info("deckungsbetrag: " + deckungsbetrag);
    L.info("ende");
  }

  
}