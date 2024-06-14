package de.htwberlin.dao;

import de.htwberlin.domain.Vertrag;
import de.htwberlin.exceptions.DataException;
import de.htwberlin.exceptions.VertragExistiertNichtException;
import de.htwberlin.service.VersicherungService;
import de.htwberlin.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public void deleteVertrag(Vertrag var1) {

    }

    @Override
    public void createVertrag(Vertrag var1) {

    }

    @Override
    public void updateVertrag(Vertrag var1) {

    }

    @Override
    public List getallVertrag() {
        return List.of();
    }

    @Override
    public int getNumberOfMale() {
        return 0;
    }

    @Override
    public int deckunsBetragPerYear(int var1) {
        return 0;
    }
}
