package info.kucharczyk.java.simple_people_base.repository.impl;

import info.kucharczyk.java.simple_people_base.repository.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseRepository<K, T> implements Repository<K, T> {
    private final Connection conn;
    private final String tableName;

    public BaseRepository(Connection connection, String tableName){
        this.conn = connection;
        this.tableName = tableName;
    }

    abstract void init() throws SQLException;

    @Override
    public boolean existsById(K id) {
        return findById(id).isPresent();
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) AS count FROM " + tableName + ";";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM " + tableName + ";";

        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
