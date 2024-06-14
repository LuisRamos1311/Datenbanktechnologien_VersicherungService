package de.htwberlin.dao;

import de.htwberlin.domain.Deckungsart;
import de.htwberlin.domain.Vertrag;
import de.htwberlin.exceptions.DataException;
import de.htwberlin.service.VersicherungService;
import de.htwberlin.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeckungsartDaoImpl implements DeckungsartDao {

    private static final Logger L = LoggerFactory.getLogger(VersicherungService.class);
    public Connection connection;

    public DeckungsartDaoImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Deckungsart getDeckungsartById(int deckungsArtById) {

        Deckungsart deckungsart = new Deckungsart();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = this.connection.prepareStatement("select * from Deckungsart where ID = ?");
            preparedStatement.setInt(1, deckungsArtById);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                deckungsart.setID(resultSet.getInt("ID"));
                deckungsart.setProdukt_FK(resultSet.getInt("PRODUKT_FK"));
                deckungsart.setKurzBez(resultSet.getString("KurzBez"));
                deckungsart.setBez(resultSet.getString("Bez"));
            }
            return deckungsart;
        } catch (SQLException sqlException) {
            L.error(sqlException.getMessage());
            throw new DataException(sqlException);
        }

    }
}
