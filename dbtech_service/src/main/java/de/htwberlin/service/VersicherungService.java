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
import de.htwberlin.domain.DeckungsBetrag;
import de.htwberlin.domain.DeckungsPreis;
import de.htwberlin.domain.Deckungsart;
import de.htwberlin.domain.Vertrag;
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

  public class Ablehnungsdaten {

    private int alter;
    private int deckungsbetrag;
    private String altersbegrenzung;

    public int getAlterbegrenzungsbetragnum() {
      return alterbegrenzungsbetragnum;
    }

    public void setAlterbegrenzungsbetragnum(int alterbegrenzungsbetragnum) {
      this.alterbegrenzungsbetragnum = alterbegrenzungsbetragnum;
    }

    private int alterbegrenzungsbetragnum;
    private String deckungsbegrenzung;
    private boolean dbMatches;
    private boolean alterMatches;


    public boolean isDbMatches() {
      return dbMatches;
    }

    public void setDbMatches(boolean dbMatches) {
      this.dbMatches = dbMatches;
    }

    public boolean isAlterMatches() {
      return alterMatches;
    }

    public void setAlterMatches(boolean alterMatches) {
      this.alterMatches = alterMatches;
    }




    public Ablehnungsdaten(int alter, int deckungsbetrag, String altersbegrenzung, String deckungsbegrenzung) {
      this.alter = alter;
      this.deckungsbetrag = deckungsbetrag;
      this.altersbegrenzung = altersbegrenzung;
      this.deckungsbegrenzung = deckungsbegrenzung;
    }

    public int getAlter() {
      return alter;
    }

    public void setAlter(int alter) {
      this.alter = alter;
    }

    public String getAltersbegrenzung() {
      return altersbegrenzung;
    }

    public void setAltersbegrenzung(String altersbegrenzung) {
      this.altersbegrenzung = altersbegrenzung;
    }

    public int getDeckungsbetrag() {
      return deckungsbetrag;
    }

    public void setDeckungsbetrag(int deckungsbetrag) {
      this.deckungsbetrag = deckungsbetrag;
    }

    public String getDeckungsbegrenzung() {
      return deckungsbegrenzung;
    }

    public void setDeckungsbegrenzung(String deckungsbegrenzung) {
      this.deckungsbegrenzung = deckungsbegrenzung;
    }

  }


  @Override
  public void createDeckung(Integer vertragsId, Integer deckungsartId, BigDecimal deckungsbetrag) {
    L.info("vertragsId: " + vertragsId);


    VertragDao vertragDao = new VertragDaoImpl(this.connection);
    Vertrag vertrag = vertragDao.getVertragById(vertragsId);
    if (vertrag.getID() == 0) {
      throw new VertragExistiertNichtException(vertragsId);
    }

    L.info("deckungsartId: " + deckungsartId);

    //DeckungsartExistiertNichtException
    DeckungsartDao deckungsartDao = new DeckungsartDaoImpl(this.connection);
    Deckungsart deckungsart = deckungsartDao.getDeckungsartById(deckungsartId);
    if (deckungsart.getID() == 0) {
      throw new DeckungsartExistiertNichtException(vertragsId);
    }

    L.info("deckungsbetrag: " + deckungsbetrag);


    //Deckungsart passt nicht zu Produkt
    if(deckungsart.getProdukt_FK() != vertrag.getPRODUKT_FK()){
      throw new DeckungsartPasstNichtZuProduktException(vertragsId, deckungsartId);
    }

    /** Commented out old approach
    try{
      PreparedStatement statement = null;
      ResultSet resultSet = null;

      statement = connection.prepareStatement("SELECT VERTRAG.ID , DECKUNGSART.ID\n" +
              "FROM Vertrag\n" +
              "JOIN Deckungsart ON vertrag.Produkt_FK = deckungsart.Produkt_FK\n" +
              "WHERE vertrag.ID = ? AND deckungsart.ID = ?");
      statement.setInt(1,vertragsId);
      statement.setInt(2, deckungsartId);
      resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        throw new DeckungsartPasstNichtZuProduktException(vertragsId, deckungsartId);
      }
    }
    catch (SQLException e){
      L.error(e.getMessage());
    } */


    //UngültigerDeckungsbetrag
    DeckungsBetragDao deckungsBetragDao = new DeckungsBetragDaoImpl(this.connection);
    DeckungsBetrag deckungsBetragObject = deckungsBetragDao.getDeckungsBetragByDeckungsart_FKAndDeckungsBetrag(deckungsartId,deckungsbetrag);
    if (deckungsBetragObject.getID() == 0) {
      throw new UngueltigerDeckungsbetragException(deckungsbetrag);
    }

    /**
    try{
      PreparedStatement statement = null;
      ResultSet resultSet = null;

      statement = connection.prepareStatement("select * from vertrag \n" +
              "join deckungsart on vertrag.produkt_fk = deckungsart.produkt_fk\n" +
              "join deckungsbetrag on deckungsart.id = deckungsbetrag.deckungsart_fk\n" +
              "where vertrag.id = ?\n" +
              "and deckungsart.id = ?\n" +
              "and deckungsbetrag.deckungsbetrag = ?");
      statement.setInt(1,vertragsId);
      statement.setInt(2,deckungsartId);
      statement.setBigDecimal(3,deckungsbetrag);
      resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        throw new UngueltigerDeckungsbetragException(deckungsbetrag);
      }
    }
    catch (SQLException sqlException) {
      L.error(sqlException.getMessage());
    }*/

    //DeckungspreisNichtVorhandenException
    DeckungsPreisDao deckungsPreisDao = new DeckungsPreisDaoImpl(this.connection);
    DeckungsPreis deckungsPreis = deckungsPreisDao.getDeckungsPreisByDeckungsbetrag_FK(deckungsBetragObject.getID());
    if (deckungsPreis.getID() == 0 ) {
      throw new DeckungspreisNichtVorhandenException(deckungsbetrag);
    }


    if (vertrag.getVERSICHERUNGSBEGINN().isBefore(deckungsPreis.getGueltig_Bis())) {
      L.error(deckungsPreis.getGueltig_Bis().toString() + vertrag.getVERSICHERUNGSBEGINN().toString());
      throw new DeckungsartNichtRegelkonformException(deckungsartId);
    }

    /**
    if (vertrag.getVERSICHERUNGSBEGINN().getYear() >= deckungsPreis.getGueltig_Bis().getYear()) {
      L.error(deckungsPreis.getGueltig_Bis().toString() + vertrag.getVERSICHERUNGSBEGINN().toString());
      throw new DeckungspreisNichtVorhandenException(deckungsbetrag);
    }*/




    /**
    try{
      PreparedStatement statement = null;
      ResultSet resultSet = null;

      statement = connection.prepareStatement("select *\n" +
              "from Deckungsbetrag\n" +
              "Join Deckungspreis\n" +
              "on Deckungsbetrag.ID = Deckungspreis.Deckungsbetrag_FK\n" +
              "Where deckungsbetrag.deckungsart_fk = ?\n" +
              "and deckungsbetrag.deckungsbetrag = ?");
      statement.setInt(1,deckungsartId);
      statement.setBigDecimal(2,deckungsbetrag);
      resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        throw new DeckungspreisNichtVorhandenException(deckungsbetrag);
      }
    }
    catch (SQLException sqlException) {
      L.error(sqlException.getMessage());
    }*/

    /**
    try{
      PreparedStatement statement = null;
      ResultSet resultSet = null;

      statement = connection.prepareStatement("select * from vertrag \n" +
              "join deckungsart on vertrag.produkt_fk = deckungsart.produkt_fk\n" +
              "join deckungsbetrag on deckungsart.id = deckungsbetrag.deckungsart_fk\n" +
              "join deckungspreis on deckungsbetrag.id = deckungspreis.deckungsbetrag_fk\n" +
              "where Vertrag.id = ?\n" +
              "and Deckungsart.id = ?\n" +
              "and deckungsbetrag.deckungsbetrag = ?\n" +
              "and Extract(year from vertrag.versicherungsbeginn) <= extract(year from deckungspreis.gueltig_bis)");
      statement.setInt(1,vertragsId);
      statement.setInt(2,deckungsartId);
      statement.setBigDecimal(3,deckungsbetrag);
      resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        throw new DeckungspreisNichtVorhandenException(deckungsbetrag);
      }
    }
    catch (SQLException sqlException) {
      L.error(sqlException.getMessage());
    }*/


    try{
      PreparedStatement statement = null;
      ResultSet resultSet = null;

      statement = connection.prepareStatement("SELECT * \n" +
              "FROM vertrag \n" +
              "JOIN produkt ON vertrag.produkt_fk = produkt.id\n" +
              "JOIN deckungsart ON produkt.id = deckungsart.produkt_fk\n" +
              "JOIN deckungsbetrag ON deckungsart.id = deckungsbetrag.deckungsart_fk\n" +
              "JOIN deckungspreis ON deckungsbetrag.id = deckungspreis.deckungsbetrag_fk\n" +
              "JOIN kunde ON vertrag.kunde_fk = kunde.id\n" +
              "left JOIN ablehnungsregel on deckungsart.id = ablehnungsregel.deckungsart_fk\n" +
              "where vertrag.id = ?\n" +
              "AND deckungsart.id = ?\n" +
              "AND deckungsbetrag.deckungsbetrag = ?\n");

      statement.setInt(1,vertragsId);
      statement.setInt(2,deckungsartId);
      statement.setBigDecimal(3,deckungsbetrag);
      resultSet = statement.executeQuery();

      List<Ablehnungsdaten> ablehnungsdatenList = new ArrayList<>();
      List<Ablehnungsdaten> OGablehnungsdatenList = new ArrayList<>();

      while (resultSet.next()) {

        Ablehnungsdaten ablehnungsdaten = new Ablehnungsdaten(Period.between(resultSet.getDate("GEBURTSDATUM").toLocalDate(),LocalDate.now()).getYears(), resultSet.getInt("DECKUNGSBETRAG"), resultSet.getString("R_ALTER"), resultSet.getString("R_Betrag"));

        try{
          ablehnungsdaten.setAlterbegrenzungsbetragnum(Integer.parseInt(ablehnungsdaten.altersbegrenzung.substring(2)));

        }catch (NullPointerException npe){

          statement = connection.prepareStatement("INSERT INTO deckung (Vertrag_FK, Deckungsart_FK, Deckungsbetrag) VALUES (?, ?, ?)");
          statement.setInt(1, vertragsId);
          statement.setInt(2, deckungsartId);
          statement.setBigDecimal(3, deckungsbetrag);


          L.info(String.valueOf(statement.executeUpdate()));
        }


        try{
          if(ablehnungsdaten.altersbegrenzung.substring(0, 1).equals(">") && ablehnungsdaten.alter > Integer.parseInt(ablehnungsdaten.altersbegrenzung.substring(2))){
            ablehnungsdaten.setAlterMatches(true);        }

          if(ablehnungsdaten.altersbegrenzung.substring(0, 1).equals("<") && ablehnungsdaten.alter < Integer.parseInt(ablehnungsdaten.altersbegrenzung.substring(2))){
            ablehnungsdaten.setAlterMatches(true);        }

          try{

            int deckungsbetrag3;

            String deckungsbegrenzung = ablehnungsdaten.deckungsbegrenzung.substring(0, 2);
            if(ablehnungsdaten.deckungsbegrenzung.equals("- -")){
              ablehnungsdaten.setDbMatches(false);
            } else {
               deckungsbetrag3 = Integer.parseInt(ablehnungsdaten.deckungsbegrenzung.substring(4));

            }


            if(ablehnungsdaten.deckungsbegrenzung.substring(0, 2).equals(">=") && ablehnungsdaten.deckungsbetrag < Integer.parseInt(ablehnungsdaten.deckungsbegrenzung.substring(3))){
              ablehnungsdaten.setDbMatches(true);
            }

            if(ablehnungsdaten.deckungsbegrenzung.substring(0, 2).equals("<=") && ablehnungsdaten.deckungsbetrag > Integer.parseInt(ablehnungsdaten.deckungsbegrenzung.substring(3))){
              ablehnungsdaten.setDbMatches(true);
            }

          }
          catch (NumberFormatException exception){}

        } catch (NullPointerException e){
          ablehnungsdaten.setAlterMatches(true);
          ablehnungsdaten.setDbMatches(true);
        }

        OGablehnungsdatenList.add(ablehnungsdaten);
      }


      OGablehnungsdatenList.stream().filter(ablehnungsdaten -> ablehnungsdaten.isAlterMatches()).max(Comparator.comparing(ablehnungsdaten -> ablehnungsdaten.alterbegrenzungsbetragnum))
              .filter(ablehnungsdaten -> ablehnungsdaten.isDbMatches()).orElseThrow(() -> new DeckungsartNichtRegelkonformException(deckungsartId));

      statement = connection.prepareStatement("INSERT INTO deckung (Vertrag_FK, Deckungsart_FK, Deckungsbetrag) VALUES (?, ?, ?)");
      statement.setInt(1, vertragsId);
      statement.setInt(2, deckungsartId);
      statement.setBigDecimal(3, deckungsbetrag);

      statement.executeUpdate();


    }
    catch (SQLException sqlException) {
      L.error(sqlException.getMessage());
    }


    L.info("ende");
  }

  
}