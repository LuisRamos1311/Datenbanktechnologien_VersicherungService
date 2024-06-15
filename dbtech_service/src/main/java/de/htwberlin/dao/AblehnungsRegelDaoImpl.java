package de.htwberlin.dao;

import de.htwberlin.domain.Ablehnungsregel;
import de.htwberlin.domain.DeckungsPreis;
import de.htwberlin.exceptions.DataException;
import de.htwberlin.service.VersicherungService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AblehnungsRegelDaoImpl implements AblehnungsRegelDao {

    private static final Logger L = LoggerFactory.getLogger(VersicherungService.class);
    public Connection connection;

    public AblehnungsRegelDaoImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Ablehnungsregel getAblehnungsRegelByDeckungsArtId(int deckungsArtId) {

        Ablehnungsregel ablehnungsRegel = new Ablehnungsregel();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = this.connection.prepareStatement("select * from ablehnungsRegel where Deckungsart_FK = ?");
            preparedStatement.setInt(1, deckungsArtId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ablehnungsRegel.setDeckungsart_FK(resultSet.getInt("Deckungsart_FK"));
                ablehnungsRegel.setLfdNr(resultSet.getInt("LfdNr"));
                ablehnungsRegel.setR_Betrag(resultSet.getString("R_Betrag"));
                ablehnungsRegel.setR_Alter(resultSet.getString("R_Alter"));
            }
            return ablehnungsRegel;
        } catch (SQLException sqlException) {
            L.error(sqlException.getMessage());
            throw new DataException(sqlException);
        }
    }

        public List<Ablehnungsregel> getAblehnungsRegelListByDeckungsArtId(int deckungsArtId) {

            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            List<Ablehnungsregel> ablehnungsregelList = new ArrayList<>();

            try {
                preparedStatement = this.connection.prepareStatement("select * from ablehnungsRegel where Deckungsart_FK = ?");
                preparedStatement.setInt(1, deckungsArtId);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Ablehnungsregel ablehnungsregel = new Ablehnungsregel();

                    ablehnungsregel.setDeckungsart_FK(resultSet.getInt("Deckungsart_FK"));
                    ablehnungsregel.setLfdNr(resultSet.getInt("LfdNr"));
                    ablehnungsregel.setR_Betrag(resultSet.getString("R_Betrag"));
                    ablehnungsregel.setR_Alter(resultSet.getString("R_Alter"));

                    ablehnungsregelList.add(ablehnungsregel);
                }
                return ablehnungsregelList;

            } catch (SQLException sqlException) {
                L.error(sqlException.getMessage());
                throw new DataException(sqlException);
            }

        }







    }

