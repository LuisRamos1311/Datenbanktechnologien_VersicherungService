package de.htwberlin.dao;

import de.htwberlin.domain.Deckung;
import de.htwberlin.domain.DeckungsPreis;
import de.htwberlin.domain.Deckungsart;
import de.htwberlin.service.VersicherungService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DeckungDaoImpl {


    private static final Logger L = LoggerFactory.getLogger(VersicherungService.class);
    public Connection connection;

    public DeckungDaoImpl(Connection connection) {
        this.connection = connection;
    }


    public void createDeckung(int vertragId, int deckungsArt, BigDecimal deckungsBetrag) {

        try{
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            preparedStatement = connection.prepareStatement("INSERT INTO deckung (Vertrag_FK, Deckungsart_FK, Deckungsbetrag) VALUES (?, ?, ?)");
            preparedStatement.setInt(1, vertragId);
            preparedStatement.setInt(2, deckungsArt);
            preparedStatement.setBigDecimal(3, deckungsBetrag);

            preparedStatement.executeUpdate();
        }catch (SQLException e){

            e.printStackTrace();
        }


    }

}
