package com.ufrn.utils;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

public class VectorType implements UserType<List<Double>> {

    @Override
    public int getSqlType() {
        return Types.OTHER; // Use OTHER for custom data types
    }

    @Override
    public Class<List<Double>> returnedClass() {
        return (Class<List<Double>>) (Class<?>) List.class;
    }

    @Override
    public boolean equals(List<Double> x, List<Double> y) {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(List<Double> x) {
        return Objects.hashCode(x);
    }

    @Override
    public List<Double> nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        PGobject pgObject = (PGobject) rs.getObject(position);
        if (pgObject == null) {
            return null;
        }
        String value = pgObject.getValue();
        // Remove brackets and split by comma
        return Stream.of(value.substring(1, value.length() - 1).split(","))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    @Override
    public void nullSafeSet(PreparedStatement st, List<Double> value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            PGobject pgObject = new PGobject();
            pgObject.setType("vector");
            String vectorString = value.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",", "[", "]"));
            pgObject.setValue(vectorString);
            st.setObject(index, pgObject);
        }
    }

    @Override
    public List<Double> deepCopy(List<Double> value) {
        if (value == null) {
            return null;
        }
        return new java.util.ArrayList<>(value);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(List<Double> value) {
        return (Serializable) deepCopy(value);
    }

    @Override
    public List<Double> assemble(Serializable cached, Object owner) {
        return deepCopy((List<Double>) cached);
    }

    @Override
    public List<Double> replace(List<Double> detached, List<Double> managed, Object owner) {
        return deepCopy(detached);
    }
}