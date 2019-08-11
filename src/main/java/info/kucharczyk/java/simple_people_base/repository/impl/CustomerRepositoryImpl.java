package info.kucharczyk.java.simple_people_base.repository.impl;

import info.kucharczyk.java.simple_people_base.repository.CustomerRepository;
import info.kucharczyk.java.simple_people_base.repository.entity.CustomerEntity;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

//it's simpler to use some ORM framework...
public class CustomerRepositoryImpl extends BaseRepository<Integer, CustomerEntity> implements CustomerRepository {
    private final Connection conn;

    private static final String CUSTOMERS_TABLE = "CUSTOMERS";

    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + CUSTOMERS_TABLE +
            "(" +
            "   ID            INTEGER auto_increment," +
            "   NAME          VARCHAR(200)," +
            "   SURNAME       VARCHAR(200)," +
            "   AGE           INTEGER" +
            ")";
    private static final String SQL_INSERT = "INSERT INTO " + CUSTOMERS_TABLE + " (NAME, SURNAME, AGE) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE " + CUSTOMERS_TABLE + " SET NAME=?, SURNAME=?, AGE=? WHERE ID=?";
    private static final String SQL_SELECT = "SELECT ID, NAME, SURNAME, AGE FROM " + CUSTOMERS_TABLE;
    private static final String SQL_SELECT_BY_ID = "SELECT ID, NAME, SURNAME, AGE FROM " + CUSTOMERS_TABLE + " WHERE ID = ?";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM " + CUSTOMERS_TABLE + " WHERE ID = ?";


    public CustomerRepositoryImpl(Connection connection) {
        super(connection, CUSTOMERS_TABLE);
        conn = connection;
    }

    @Override
    public void init() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute(SQL_CREATE);
    }

    @Override
    public void delete(CustomerEntity entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteById(Integer id) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE_BY_ID);
            pstmt.setInt(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Iterable<CustomerEntity> findAll() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_SELECT);
            List<CustomerEntity> result = new LinkedList<>();
            while (rs.next()) {
                CustomerEntity entity = mapToEntity(rs);
                result.add(entity);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<CustomerEntity> findById(Integer id) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                CustomerEntity entity = mapToEntity(rs);
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private CustomerEntity mapToEntity(ResultSet rs) throws SQLException {
        CustomerEntity result = new CustomerEntity();
        result.setId(rs.getInt("ID"));
        result.setName(rs.getString("NAME"));
        result.setSurname(rs.getString("SURNAME"));
        result.setAge(rs.getInt("AGE"));
        if(rs.wasNull()){
            result.setAge(null);
        }
        return result;
    }

    private void insert(CustomerEntity entity) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, entity.getName());
        pstmt.setString(2, entity.getSurname());
        pstmt.setNull(3, Types.INTEGER);
        if (entity.getAge() != null)
            pstmt.setInt(3, entity.getAge());

        pstmt.executeUpdate();
        ResultSet rs = pstmt.getGeneratedKeys();
        while (rs.next()){
            entity.setId(rs.getInt("ID"));
        }
    }

    private void update(CustomerEntity entity) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE);
        pstmt.setString(1, entity.getName());
        pstmt.setString(2, entity.getSurname());
        pstmt.setNull(3, Types.INTEGER);
        if (entity.getAge() != null) {
            pstmt.setInt(3, entity.getAge());
        }
        pstmt.setInt(4, entity.getId());

        pstmt.executeUpdate();
    }

    @Override
    public CustomerEntity save(CustomerEntity entity) {
        try {
            if (entity.getId() == null) { // create
                insert(entity);
            } else { // update
                update(entity);
            }
            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
