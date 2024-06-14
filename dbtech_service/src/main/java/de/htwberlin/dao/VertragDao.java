package de.htwberlin.dao;

import de.htwberlin.domain.Vertrag;

import java.util.List;

public interface VertragDao {
    Vertrag getVertragById(int vertragById);

    void deleteVertrag(Vertrag var1);

    void createVertrag(Vertrag var1);

    void updateVertrag(Vertrag var1);

    List getallVertrag();

    int getNumberOfMale();

    int deckunsBetragPerYear(int var1);
}
