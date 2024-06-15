package de.htwberlin.service;

/**
 * @author Ingo Classen
 */

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.htwberlin.dao.*;
import de.htwberlin.domain.*;
import de.htwberlin.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    VertragDao vertragDao = new VertragDaoImpl(this.connection);
    Vertrag vertrag = vertragDao.getVertragById(vertragsId);
    if (vertrag.getID() == 0) {
      L.error("Vertrag mit ID " + vertragsId + " nicht gefunden");
      throw new VertragExistiertNichtException(vertragsId);
    }

    //DeckungsartExistiertNichtException
    DeckungsartDao deckungsartDao = new DeckungsartDaoImpl(this.connection);
    Deckungsart deckungsart = deckungsartDao.getDeckungsartById(deckungsartId);
    if (deckungsart.getID() == 0) {
      L.error("Deckungsart mit ID " + deckungsartId + " nicht gefunden");
      throw new DeckungsartExistiertNichtException(vertragsId);
    }

    //Deckungsart passt nicht zu Produkt
    if(deckungsart.getProdukt_FK() != vertrag.getPRODUKT_FK()){
      L.error("Vertrag mit ID " + vertragsId + "und Deckungsart mit ID " + deckungsartId + " passen nicht zusammen" );
      throw new DeckungsartPasstNichtZuProduktException(vertragsId, deckungsartId);
    }

    //UngültigerDeckungsbetrag
    DeckungsBetragDao deckungsBetragDao = new DeckungsBetragDaoImpl(this.connection);
    DeckungsBetrag deckungsBetragObject = deckungsBetragDao.getDeckungsBetragByDeckungsart_FKAndDeckungsBetrag(deckungsartId,deckungsbetrag);
    if (deckungsBetragObject.getID() == 0) {
      L.error("Ungültiger Deckungsbetrag" );
      throw new UngueltigerDeckungsbetragException(deckungsbetrag);
    }

    //DeckungspreisNichtVorhandenException
    DeckungsPreisDao deckungsPreisDao = new DeckungsPreisDaoImpl(this.connection);

    List<DeckungsPreis> deckungsPreisList = deckungsPreisDao.getDeckungsPreisListByDeckungsbetrag_FK(deckungsBetragObject.getID())
            .stream()
            .filter(deckungsPreisObj -> vertrag.getVERSICHERUNGSBEGINN().isBefore(deckungsPreisObj.getGueltig_Bis())
                    || vertrag.getVERSICHERUNGSBEGINN().isEqual(deckungsPreisObj.getGueltig_Bis()))
            .toList();

    if(deckungsPreisList.isEmpty()) {
      L.error("DeckungsPreis nicht gefunden" );
      throw new DeckungspreisNichtVorhandenException(deckungsbetrag);
    }

    AblehnungsRegelDao ablehnungsRegelDao = new AblehnungsRegelDaoImpl(this.connection);
    List<Ablehnungsregel> ablehnungsRegelList = ablehnungsRegelDao.getAblehnungsRegelListByDeckungsArtId(deckungsartId);

    KundeDao kundeDao = new KundeDaoImpl(this.connection);
    Kunde kunde = kundeDao.getKundeById(vertrag.getKUNDE_FK());

    if(!ablehnungsRegelList.isEmpty()) {
      ablehnungsRegelList.stream()
              .filter(ablehnungsdaten -> this.doesAgeMatch(kunde.getGeburtsdatum(), ablehnungsdaten.getR_Alter()))
              .max(Comparator.comparing(ablehnungsdaten -> ablehnungsdaten.getR_Alter().substring(2)))
              .filter(ablehnungsdaten -> this.doesDeckungsBetragMatch(deckungsbetrag.intValue(),ablehnungsdaten.getR_Betrag()))
              .orElseThrow(() -> new DeckungsartNichtRegelkonformException(deckungsartId));
    }

    DeckungDaoImpl deckungDao = new DeckungDaoImpl(this.connection);
    deckungDao.createDeckung(vertragsId,deckungsartId,deckungsbetrag);
  }

  private boolean doesAgeMatch(LocalDate birthDate, String ageCondition){
    if(ageCondition.charAt(0) == '>' && Period.between(birthDate, LocalDate.now()).getYears() > Integer.parseInt(ageCondition.substring(2))){
      return true;    }
      return ageCondition.charAt(0) == '<' && Period.between(birthDate, LocalDate.now()).getYears() < Integer.parseInt(ageCondition.substring(2));
  }

  private boolean doesDeckungsBetragMatch(Integer deckungsbetrag, String deckungsBetragCondition){
    if(deckungsBetragCondition.startsWith(">=") && deckungsbetrag < Integer.parseInt(deckungsBetragCondition.substring(3))){
      return true;     }
    return deckungsBetragCondition.startsWith("<=") && deckungsbetrag > Integer.parseInt(deckungsBetragCondition.substring(3));
  }
}