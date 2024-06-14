package de.htwberlin.test;

import java.math.BigDecimal;
import java.net.URL;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.csv.CsvURLDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.exceptions.DeckungsartExistiertNichtException;
import de.htwberlin.exceptions.DeckungsartNichtRegelkonformException;
import de.htwberlin.exceptions.DeckungsartPasstNichtZuProduktException;
import de.htwberlin.exceptions.DeckungspreisNichtVorhandenException;
import de.htwberlin.exceptions.UngueltigerDeckungsbetragException;
import de.htwberlin.exceptions.VertragExistiertNichtException;
import de.htwberlin.service.IVersicherungService;
import de.htwberlin.service.VersicherungService;
import de.htwberlin.test.utils.DbUnitUtils;
import de.htwberlin.utils.DbCred;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VersicherungServiceTest {
  private static final Logger L = LoggerFactory.getLogger(VersicherungServiceTest.class);
  private static IDatabaseTester dbTester;
  private static IDatabaseConnection dbTesterCon = null;
  private static String dataDirPath = "de/htwberlin/test/data/";
  private static URL dataFeedUrl = ClassLoader.getSystemResource(dataDirPath);
  private static IDataSet feedDataSet = null;

  private static IVersicherungService vService = new VersicherungService();

  @BeforeClass
  public static void setUp() {
    L.debug("start");
    try {
      dbTester = new JdbcDatabaseTester(DbCred.driverClass, DbCred.url, DbCred.user, DbCred.password, DbCred.schema);
      dbTesterCon = dbTester.getConnection();
      feedDataSet = new CsvURLDataSet(dataFeedUrl);
      dbTester.setDataSet(feedDataSet);
      DatabaseOperation.CLEAN_INSERT.execute(dbTesterCon, feedDataSet);
      vService.setConnection(dbTesterCon.getConnection());
    } catch (Exception e) {
      DbUnitUtils.closeDbUnitConnectionQuietly(dbTesterCon);
      throw new RuntimeException(e);
    }
  }

  @AfterClass
  public static void tearDown() throws Exception {
    L.debug("start");
    DbUnitUtils.closeDbUnitConnectionQuietly(dbTesterCon);
  }

  /**
   * Vertrag existiert nicht.
   * 
   */
  @org.junit.Test(expected = VertragExistiertNichtException.class)
  public void createDeckung01() {
    vService.createDeckung(99, 99, BigDecimal.valueOf(0));
  }

  /**
   * Deckungsart existiert nicht.
   * 
   */
  @org.junit.Test(expected = DeckungsartExistiertNichtException.class)
  public void createDeckung02() {
    vService.createDeckung(5, 99, BigDecimal.valueOf(0));
  }

  /**
   * Deckungsart passt nicht zu Produkt. Deckungsart 1 (Haftung) passt zu KFZV,
   * der Vertrag 5 ist aber fuer HRV.
   * 
   */
  @org.junit.Test(expected = DeckungsartPasstNichtZuProduktException.class)
  public void createDeckung03() {
    vService.createDeckung(5, 1, BigDecimal.valueOf(0));
  }

  /**
   * Deckungsbetrag ist ungueltig für Deckungsart 6 (Fahrraddiebstahl), da kein
   * Deckungsbetragsdatensatz fuer die Deckungsart vorliegt. Fuer Deckungsart 6
   * gibt es keinen Datenssatz in der Tabelle Deckungsbetrag.
   * 
   */
  @org.junit.Test(expected = UngueltigerDeckungsbetragException.class)
  public void createDeckung04() {
    vService.createDeckung(5, 6, BigDecimal.valueOf(0));
  }

  /**
   * Deckungsbetrag 2000 ist ungueltig für Deckungsart 5 (Glasbruch), da nur ein
   * Deckungsbetrag von 1500 fuer die Deckungsart vorliegt.
   * 
   */
  @org.junit.Test(expected = UngueltigerDeckungsbetragException.class)
  public void createDeckung05() {
    vService.createDeckung(5, 5, BigDecimal.valueOf(2000));
  }

  /**
   * Deckungsbetrag 1500 ist zwar gueltig fuer Deckungsart 5 (Glasbruch), es gibt
   * aber keinen Deckungspreis dafuer.
   * 
   */
  @org.junit.Test(expected = DeckungspreisNichtVorhandenException.class)
  public void createDeckung06() {
    vService.createDeckung(5, 5, BigDecimal.valueOf(1500));
  }

  /**
   * Deckungsbetrag 150000 ist gueltig fuer Deckungsart 4 (Brandschaden), da ein
   * Deckungsbetrag von 150000 existiert. Einen Deckungspreis fuer diesen
   * Deckungsbetrag gibt es auch. Allerdings wird dieser Preis nur bis Ende 2018
   * angeboten. Der Versicherungsbeginn fuer Vertrag 5 liegt aber in 2019.
   * 
   */
  @org.junit.Test(expected = DeckungspreisNichtVorhandenException.class)
  public void createDeckung07() {
    vService.createDeckung(5, 4, BigDecimal.valueOf(150000));
  }

  /**
   * Vertrag 6 (KFZV) hat einen Kunden, der noch kein Jahr alt ist. Deckungsart 1
   * (Haftung) passt zu KFZV und Deckungsbetrag 100 Mio ebenfalls. Haftung wird
   * aber fuer Kunden unter 18 nicht angeboten.
   * 
   */
  @org.junit.Test(expected = DeckungsartNichtRegelkonformException.class)
  public void createDeckung08() {
    vService.createDeckung(6, 1, BigDecimal.valueOf(100000000));
  }

  /**
   * Vertrag 7 (LBV) hat einen Kunden, der der aelter als 90 Jahre ist.
   * Deckungsart 3 (Tod) passt zu LBV und Deckungsbetrag 100 Tsd ebenfalls. Tod
   * wird aber fuer Kunden ueber 90 nicht angeboten.
   * 
   */
  @org.junit.Test(expected = DeckungsartNichtRegelkonformException.class)
  public void createDeckung09() {
    vService.createDeckung(7, 3, BigDecimal.valueOf(100000));
  }

  /**
   * Vertrag 8 (LBV) hat einen Kunden, der der aelter als 70 Jahre ist.
   * Deckungsart 3 (Tod) passt zu LBV und Deckungsbetrag 200 Tsd ebenfalls. Tod
   * mit einem Betrag von 200 Tsd wird aber fuer Kunden ueber 70 nicht angeboten.
   * 
   */
  @org.junit.Test(expected = DeckungsartNichtRegelkonformException.class)
  public void createDeckung10() {
    vService.createDeckung(8, 3, BigDecimal.valueOf(200000));
  }

  /**
   * Vertrag 9 (LBV) hat einen Kunden, der der aelter als 60 Jahre ist.
   * Deckungsart 3 (Tod) passt zu LBV und Deckungsbetrag 300 Tsd ebenfalls. Tod
   * mit einem Betrag von 300 Tsd wird aber fuer Kunden ueber 60 nicht angeboten.
   * 
   */
  @org.junit.Test(expected = DeckungsartNichtRegelkonformException.class)
  public void createDeckung11() {
    vService.createDeckung(9, 3, BigDecimal.valueOf(300000));
  }

  /**
   * Die folgenden Deckungserzeugungen sind ok und muessen in der Datebank
   * eingetragen werden.
   * 
   */
  @org.junit.Test
  public void createDeckung12() throws Exception {
    Integer[] vertragsIds = new Integer[] {5, 8, 9 };
    Integer[] deckungsartIds = new Integer[] {4, 3, 3 };
    BigDecimal[] deckungsbetraege = new BigDecimal[] {BigDecimal.valueOf(50000), BigDecimal.valueOf(100000),
        BigDecimal.valueOf(200000) };
    for (int i = 0; i < 3; i++) {
      vService.createDeckung(vertragsIds[i], deckungsartIds[i], deckungsbetraege[i]);
    }

    // Hole Deckungsdatensaetze aus der Datenbank
    QueryDataSet databaseDataSet = new QueryDataSet(dbTesterCon);
    String sql = "select * from Deckung where Vertrag_FK in (5, 8, 9) order by Vertrag_FK, Deckungsart_FK";
    databaseDataSet.addTable("Deckung", sql);
    ITable tblDeckung = databaseDataSet.getTable("Deckung");

    // Wurde die richtige Anzahl an Datensaetzen in die Datenbank eingetragen?
    Assert.assertEquals("Falsche Anzahl Zeilen", 3, tblDeckung.getRowCount());

    // Wurden die richtigen Werte eingetragen?
    for (int i = 0; i < 3; i++) {
      Integer vertragsId = ((BigDecimal) tblDeckung.getValue(i, "Vertrag_FK")).intValue();
      Integer deckungsartId = ((BigDecimal) tblDeckung.getValue(i, "Deckungsart_FK")).intValue();
      BigDecimal deckungsbetrag = (BigDecimal) tblDeckung.getValue(i, "Deckungsbetrag");
      Assert.assertEquals("Falsche vertragsId", vertragsIds[i], vertragsId);
      Assert.assertEquals("Falsche deckungsartId", deckungsartIds[i], deckungsartId);
      Assert.assertEquals("Falscher deckungsbetrag", deckungsbetraege[i], deckungsbetrag);
    }

  }

}
