package de.htwberlin.dao;

import de.htwberlin.domain.Ablehnungsregel;
import de.htwberlin.domain.Kunde;
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

public class KundeDaoImpl implements KundeDao {

    private static final Logger L = LoggerFactory.getLogger(VersicherungService.class);
    public Connection connection;

    public KundeDaoImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Kunde getKundeById(int kundeId) {

        Kunde kunde = new Kunde();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = this.connection.prepareStatement("select * from kunde where ID = ?");
            preparedStatement.setInt(1, kundeId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                kunde.setId(resultSet.getInt("ID"));
                kunde.setName(resultSet.getString("Name"));
                kunde.setGeburtsdatum(resultSet.getDate("Geburtsdatum").toLocalDate());
            }
            return kunde;
        } catch (SQLException sqlException) {
            L.error(sqlException.getMessage());
            throw new DataException(sqlException);
        }
    }
    }

