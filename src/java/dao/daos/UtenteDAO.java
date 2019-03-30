package dao.daos;

import dao.utils.ConnectionManager;
import dao.utils.SQLQuery;
import dao.dto.Utente;
import dao.exceptions.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAO {

    private final ConnectionManager connectionManager;

    public UtenteDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Crea un utente e lo inserisce nel DB, l'ID dell'utente viene generato dal
     * DB e viene inserito nell'oggetto utente.
     *
     * @param utente l'utente da creare
     * @return ID del nuovo utente
     */
    public int create(Utente utente) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        int id = -1;

        try {
            st = conn.prepareStatement(SQLQuery.CREATE_UTENTE, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, utente.getUsername());
            st.setString(2, utente.getPassword());
            st.setBoolean(3, utente.isIsAdmin());
            st.executeUpdate();

            rs = st.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
                utente.setID(id);
            } else {
                utente.setID(null);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(rs, st, conn);
        }

        return id;
    }

    /**
     * Ritorna le informazioni dell'utente con l'ID specificato.
     *
     * @param id ID dell'utente
     * @return utente con l'ID specificato
     */
    public Utente findByID(int id) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet result = null;
        Utente utente = null;

        try {
            st = conn.prepareStatement(SQLQuery.FIND_BY_ID_UTENTE);
            st.setInt(1, id);
            result = st.executeQuery();

            if (result.next()) {
                utente = new Utente(result.getInt("ID"), result.getString("Username"), result.getString("Password"), result.getBoolean("isAdmin"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }

        return utente;
    }

    /**
     * Ritorna le informazioni dell'utente con lo username specificato.
     *
     * @param username lo username dell'utente
     * @return utente con lo username specificato
     */
    public Utente findByUsername(String username) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet result = null;
        Utente utente = null;

        try {
            st = conn.prepareStatement(SQLQuery.FIND_BY_USERNAME_UTENTE);
            st.setString(1, username);
            result = st.executeQuery();

            if (result.next()) {
                utente = new Utente(result.getInt("ID"), username, result.getString("Password"), result.getBoolean("isAdmin"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }

        return utente;
    }

    /**
     * Ritorna le informazioni di tutti gli utenti
     *
     * @return una lista di tutti gli utenti
     */
    public List<Utente> findAll() {
        Connection conn = connectionManager.getConnection();
        Statement st = null;
        ResultSet result = null;
        List<Utente> utenti = new ArrayList<>();

        try {
            st = conn.createStatement();
            result = st.executeQuery(SQLQuery.FIND_ALL_UTENTE);

            while (result.next()) {
                utenti.add(new Utente(result.getInt("ID"), result.getString("Username"), result.getString("Password"), result.getBoolean("isAdmin")));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }

        return utenti;
    }

    /**
     * Setta tutti i dati dell'utente eccetto l'ID, l'utente da modificare è
     * scelto in base all'ID.
     *
     * @param utente oggetto utente con i dati modificati
     */
    public void update(Utente utente) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(SQLQuery.UPDATE_UTENTE);
            st.setString(1, utente.getUsername());
            st.setString(2, utente.getPassword());
            st.setBoolean(3, utente.isIsAdmin());
            st.setInt(4, utente.getID());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(rs, st, conn);
        }
    }

    /**
     * Cancella l'utente dal database assieme ai dati ad esso correlati.
     *
     * @param id ID dell'utente da eliminare
     */
    public void deleteByID(int id) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet res = null;

        try {
            st = conn.prepareStatement(SQLQuery.DELETE_BY_ID_UTENTE);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(res, st, conn);
        }
    }

    /**
     * Cancella l'utente dal database assieme ai dati ad esso correlati.
     *
     * @param username username dell'utente da eliminare
     */
    public void deleteByUsername(String username) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet res = null;

        try {
            st = conn.prepareStatement(SQLQuery.DELETE_BY_USERNAME_UTENTE);
            st.setString(1, username);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(res, st, conn);
        }
    }
}
