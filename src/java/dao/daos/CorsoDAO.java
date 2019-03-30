package dao.daos;

import dao.utils.ConnectionManager;
import dao.dto.Corso;
import dao.utils.SQLQuery;
import dao.exceptions.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CorsoDAO {

    private final ConnectionManager connectionManager;

    public CorsoDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Crea un corso e lo inserisce nel DB, l'ID del corso viene generato dal DB
     * e viene inserito nell'oggetto corso.
     *
     * @param corso il corso da creare
     * @return ID del nuovo corso
     */
    public int create(Corso corso) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        int id = -1;

        try {
            st = conn.prepareStatement(SQLQuery.CREATE_CORSO, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, corso.getTitolo());
            st.executeUpdate();

            rs = st.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
                corso.setID(id);
            } else {
                corso.setID(null);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(rs, st, conn);
        }

        return id;
    }

    /**
     * Ritorna le informazioni del corso con l'ID specificato.
     *
     * @param id ID del corso
     * @return corso con l'ID specificato
     */
    public Corso findByID(int id) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet result = null;
        Corso corso = null;

        try {
            st = conn.prepareStatement(SQLQuery.FIND_BY_ID_CORSO);
            st.setInt(1, id);
            result = st.executeQuery();

            if (result.next()) {
                corso = new Corso(result.getInt("ID"), result.getString("Titolo"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }

        return corso;
    }

    /**
     * Ritorna le informazioni del corso con il titolo specificato.
     *
     * @param titolo il titolo del corso
     * @return corso con il titolo specificato
     */
    public Corso findByTitolo(String titolo) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet result = null;
        Corso corso = null;

        try {
            st = conn.prepareStatement(SQLQuery.FIND_BY_TITOLO_CORSO);
            st.setString(1, titolo);
            result = st.executeQuery();

            if (result.next()) {
                corso = new Corso(result.getInt("ID"), result.getString("Titolo"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }

        return corso;
    }

    /**
     * Ritorna le informazioni di tutti i corsi
     *
     * @return una lista di tutti i corsi
     */
    public List<Corso> findAll() {
        Connection conn = connectionManager.getConnection();
        Statement st = null;
        ResultSet result = null;
        List<Corso> corsi = new ArrayList<>();

        try {
            st = conn.createStatement();
            result = st.executeQuery(SQLQuery.FIND_ALL_CORSO);

            while (result.next()) {
                corsi.add(new Corso(result.getInt("ID"), result.getString("Titolo")));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }

        return corsi;
    }
    
    /**
     * Ritorna le informazioni di tutti i corsi attivi
     * 
     * @return un insieme di corsi attivi (HashSet per ragioni di performance)
     */
    public Set<Corso> findAllActive() {
        Connection conn = connectionManager.getConnection();
        Statement st = null;
        ResultSet result = null;
        Set<Corso> corsi = new HashSet<>();

        try {
            st = conn.createStatement();
            result = st.executeQuery(SQLQuery.FIND_ALL_ACTIVE_CORSO);

            while (result.next()) {
                corsi.add(new Corso(result.getInt("ID"), result.getString("Titolo")));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }

        return corsi;
    }

    /**
     * Setta tutti i dati del corso eccetto l'ID, il corso da modificare è
     * scelto in base all'ID.
     *
     * @param corso oggetto corso con i dati modificati
     */
    public void update(Corso corso) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(SQLQuery.UPDATE_CORSO);
            st.setString(1, corso.getTitolo());
            st.setInt(2, corso.getID());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(rs, st, conn);
        }
    }

    /**
     * Cancella il corso SENZA rimuoverlo dal DB, ovvero rimuove tutte le
     * docenze di quel corso.
     *
     * @param id ID del corso da eliminare
     */
    public void deleteByID(int id) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet res = null;

        try {
            st = conn.prepareStatement(SQLQuery.DELETE_BY_CORSO_DOCENZA);
            st.setInt(1, id);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.AUTO_UPDATE_STATO_PRENOTAZIONE);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.UPDATE_PRENOTAZIONI_ATTIVE_BY_CORSO);
            st.setInt(1, id);
            st.executeUpdate();
            
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(res, st, conn);
        }
    }

    /**
     * Cancella il corso SENZA rimuoverlo dal DB, ovvero rimuove tutte le
     * docenze di quel corso.
     *
     * @param titolo titolo del corso da eliminare
     */
    public void deleteByTitolo(String titolo) {
        Corso corso = findByTitolo(titolo);
        if (corso != null) {
            deleteByID(corso.getID());
        }
    }
}
