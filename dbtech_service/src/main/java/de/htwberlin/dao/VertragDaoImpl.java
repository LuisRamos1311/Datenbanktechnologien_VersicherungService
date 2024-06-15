package de.htwberlin.dao;

import de.htwberlin.domain.Vertrag;
import de.htwberlin.exceptions.DataException;
import de.htwberlin.exceptions.VertragExistiertNichtException;
import de.htwberlin.service.VersicherungService;
import de.htwberlin.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VertragDaoImpl implements VertragDao {

    private static final Logger L = LoggerFactory.getLogger(VersicherungService.class);
    public Connection connection;

    public VertragDaoImpl(Connection connection) {
        this.connection = connection;
    }


    public Vertrag getVertragById(int vertragId) {
        Vertrag vertrag = new Vertrag();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = this.connection.prepareStatement("select * from Vertrag where ID = ?");
            preparedStatement.setInt(1, vertragId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                vertrag.setID(resultSet.getInt("ID"));
                vertrag.setPRODUKT_FK(resultSet.getInt("PRODUKT_FK"));
                vertrag.setKUNDE_FK(resultSet.getInt("KUNDE_FK"));
                vertrag.setVERSICHERUNGSBEGINN(DateUtils.sqlDate2LocalDate(resultSet.getDate("VERSICHERUNGSBEGINN")));
                vertrag.setVERSICHERUNGSENDE(DateUtils.sqlDate2LocalDate(resultSet.getDate("VERSICHERUNGSENDE")));
            }
            return vertrag;
        } catch (SQLException sqlException) {
            L.error(sqlException.getMessage());
            throw new DataException(sqlException);
        }
    }

    @Override
    public void deleteVertrag(Vertrag vertrag) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = this.connection.prepareStatement("delete * from Vertrag where ID = ?");
            preparedStatement.setInt(1, vertrag.getID());
            preparedStatement.executeQuery();

        } catch (SQLException sqlException) {
            L.error(sqlException.getMessage());
            throw new DataException(sqlException);
        }

    }

    @Override
    public void createVertrag(Vertrag vertrag) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = this.connection.prepareStatement("INSERT INTO Vertrag VALUES ?,?,?,?,?");
            preparedStatement.setInt(1, vertrag.getID());
            preparedStatement.setInt(2, vertrag.getPRODUKT_FK());
            preparedStatement.setInt(3, vertrag.getKUNDE_FK());
            preparedStatement.setDate(4, Date.valueOf(vertrag.getVERSICHERUNGSBEGINN()));
            preparedStatement.setDate(5, Date.valueOf(vertrag.getVERSICHERUNGSENDE()));

            preparedStatement.executeQuery();

        } catch (SQLException sqlException) {
            L.error(sqlException.getMessage());
            throw new DataException(sqlException);
        }

    }

    @Override
    public void updateVertrag(Vertrag vertrag) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = this.connection.prepareStatement("UPDATE Vertrag SET PRODUKT_FK = ?, KUNDE_FK = ?, VERSICHERUNGSBEGINN = ?, VERSICHERUNGSENDE = ? WHERE ID = ?");
            preparedStatement.setInt(1, vertrag.getPRODUKT_FK());
            preparedStatement.setInt(2, vertrag.getKUNDE_FK());
            preparedStatement.setDate(3, Date.valueOf(vertrag.getVERSICHERUNGSBEGINN()));
            preparedStatement.setDate(4, Date.valueOf(vertrag.getVERSICHERUNGSENDE()));

            preparedStatement.executeQuery();

        } catch (SQLException sqlException) {
            L.error(sqlException.getMessage());
            throw new DataException(sqlException);
        }
    }

    @Override
    public List<Vertrag> getallVertrag() {
        List<Vertrag> vertragsList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = this.connection.prepareStatement("select * from Vertrag");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                Vertrag vertrag = new Vertrag();

                vertrag.setID(resultSet.getInt("ID"));
                vertrag.setPRODUKT_FK(resultSet.getInt("PRODUKT_FK"));
                vertrag.setKUNDE_FK(resultSet.getInt("KUNDE_FK"));
                vertrag.setVERSICHERUNGSBEGINN(DateUtils.sqlDate2LocalDate(resultSet.getDate("VERSICHERUNGSBEGINN")));
                vertrag.setVERSICHERUNGSENDE(DateUtils.sqlDate2LocalDate(resultSet.getDate("VERSICHERUNGSENDE")));

                vertragsList.add(vertrag);
            }

            return vertragsList;

        } catch (SQLException sqlException) {
            L.error(sqlException.getMessage());
            throw new DataException(sqlException);
        }
    }

    @Override
    public int getNumberOfMale() {
        //kein Geschlecht in Tabelle angegeben
        return 0;
    }

    @Override
    public int deckunsBetragPerYear(int var1) {
        //verstehe die Aufgabenstellung hier nicht
        return 0;
    }
}
