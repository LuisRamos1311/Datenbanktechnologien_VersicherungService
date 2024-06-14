package de.htwberlin.dao;

import de.htwberlin.domain.DeckungsBetrag;
import de.htwberlin.domain.DeckungsPreis;
import de.htwberlin.exceptions.DataException;
import de.htwberlin.service.VersicherungService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeckungsPreisDaoImpl implements DeckungsPreisDao {

    private static final Logger L = LoggerFactory.getLogger(VersicherungService.class);
    public Connection connection;

    public DeckungsPreisDaoImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public DeckungsPreis getDeckungsPreisByDeckungsbetrag_FK(int deckungsBetragId) {

        DeckungsPreis deckungsPreis = new DeckungsPreis();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = this.connection.prepareStatement("select * from Deckungspreis where Deckungsbetrag_FK=?");
            preparedStatement.setInt(1, deckungsBetragId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                deckungsPreis.setID(resultSet.getInt("ID"));
                deckungsPreis.setDeckungsbetrag_FK(resultSet.getInt("Deckungsbetrag_FK"));
                deckungsPreis.setGueltig_Von(resultSet.getDate("Gueltig_Von").toLocalDate());
                deckungsPreis.setGueltig_Bis(resultSet.getDate("Gueltig_Bis").toLocalDate());
                deckungsPreis.setPreis(resultSet.getInt("Preis"));

            }
            return deckungsPreis;
        } catch (SQLException sqlException) {
            L.error(sqlException.getMessage());
            throw new DataException(sqlException);
        }

    }
}
