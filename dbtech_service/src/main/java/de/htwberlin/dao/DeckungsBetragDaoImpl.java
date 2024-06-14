package de.htwberlin.dao;

import de.htwberlin.domain.Deckungsart;
import de.htwberlin.domain.DeckungsBetrag;
import de.htwberlin.exceptions.DataException;
import de.htwberlin.service.VersicherungService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeckungsBetragDaoImpl implements DeckungsBetragDao {

    private static final Logger L = LoggerFactory.getLogger(VersicherungService.class);
    public Connection connection;

    public DeckungsBetragDaoImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public DeckungsBetrag getDeckungsBetragByDeckungsart_FKAndDeckungsBetrag(int deckungsArtId, BigDecimal deckungsBetrag) {

        DeckungsBetrag deckungsBetragObject = new DeckungsBetrag();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = this.connection.prepareStatement("select * from Deckungsbetrag where Deckungsart_FK=? and Deckungsbetrag=?");
            preparedStatement.setInt(1, deckungsArtId);
            preparedStatement.setBigDecimal(2, deckungsBetrag);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                deckungsBetragObject.setID(resultSet.getInt("ID"));
                deckungsBetragObject.setDeckungsart_FK(resultSet.getInt("Deckungsart_FK"));
                deckungsBetragObject.setDeckungsbetrag(resultSet.getBigDecimal("Deckungsbetrag"));
            }
            return deckungsBetragObject;
        } catch (SQLException sqlException) {
            L.error(sqlException.getMessage());
            throw new DataException(sqlException);
        }

    }
}
