package info.kucharczyk.java.simple_people_base.repository.impl;

import info.kucharczyk.java.simple_people_base.repository.ContactRepository;
import info.kucharczyk.java.simple_people_base.repository.entity.ContactEntity;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

//it's simpler to use some ORM framework...
public class ContactRepositoryImpl extends BaseRepository<Integer, ContactEntity> implements ContactRepository {
    private final Connection conn;

    private static final String CONTACTS_TABLE = "CONTACTS";

    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + CONTACTS_TABLE +
            "(" +
            "   ID            INTEGER auto_increment," +
            "   ID_CUSTOMER   INTEGER," +
            "   TYPE          INTEGER," +
            "   CONTACT       VARCHAR(200)" +
            ")";
    private static final String SQL_INSERT = "INSERT INTO " + CONTACTS_TABLE + " (ID_CUSTOMER, TYPE, CONTACT) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE " + CONTACTS_TABLE + " SET ID_CUSTOMER=?, TYPE=?, CONTACT=? WHERE ID=?";
    private static final String SQL_SELECT = "SELECT ID, ID_CUSTOMER, TYPE, CONTACT FROM " + CONTACTS_TABLE;
    private static final String SQL_SELECT_BY_ID = "SELECT ID, ID_CUSTOMER, TYPE, CONTACT FROM " + CONTACTS_TABLE + " WHERE ID = ?";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM " + CONTACTS_TABLE + " WHERE ID = ?";

    public ContactRepositoryImpl(Connection connection){
        super(connection, CONTACTS_TABLE);
        conn = connection;
    }

    @Override
    public void init() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute(SQL_CREATE);
    }

    @Override
    public void delete(ContactEntity entity) {
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
    public Iterable<ContactEntity> findAll() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_SELECT);
            List<ContactEntity> result = new LinkedList<>();
            while (rs.next()) {
                ContactEntity entity = mapToEntity(rs);
                result.add(entity);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<ContactEntity> findById(Integer id) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ContactEntity entity = mapToEntity(rs);
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private ContactEntity mapToEntity(ResultSet rs) throws SQLException {
        ContactEntity result = new ContactEntity();
        result.setId(rs.getInt("ID"));
        result.setId_customer(rs.getInt("ID_CUSTOMER"));
        result.setType(rs.getInt("TYPE"));
        result.setContact(rs.getString("CONTACT"));
        return result;
    }

    private void insert(ContactEntity entity) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, entity.getId_customer());
        pstmt.setInt(2, entity.getType());
        pstmt.setString(3, entity.getContact());

        pstmt.executeUpdate();
        ResultSet rs = pstmt.getGeneratedKeys();
        while (rs.next()){
            entity.setId(rs.getInt("ID"));
        }
    }

    private void update(ContactEntity entity) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE);
        pstmt.setInt(1, entity.getId_customer());
        pstmt.setInt(2, entity.getType());
        pstmt.setString(3, entity.getContact());
        pstmt.setInt(4, entity.getId());

        pstmt.executeUpdate();
    }

    @Override
    public ContactEntity save(ContactEntity entity) {
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
