package dao.daos;

import dao.utils.ConnectionManager;
import dao.dto.Corso;
import dao.dto.Docente;
import dao.dto.Docenza;
import dao.utils.SQLQuery;
import dao.exceptions.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DocenzaDAO {

    private final ConnectionManager connectionManager;

    public DocenzaDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Crea una docenza e la inserisce nel DB, l'ID della docenza è composto
     * dall'ID del docente e del corso che devono essere pre-esistenti.
     *
     * @param docenza la docenza da creare
     */
    public void create(Docenza docenza) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(SQLQuery.CREATE_DOCENZA);
            st.setInt(1, docenza.getCorso().getID());
            st.setInt(2, docenza.getDocente().getID());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(rs, st, conn);
        }
    }

    /**
     * Ritorna le informazioni della docenza con l'ID specificato (metodo utile
     * per controllare l'esistenza di una docenza).
     *
     * @param idDocente ID del docente
     * @param idCorso ID del corso
     * @return docenza con l'ID specificato
     */
    public Docenza findByID(int idDocente, int idCorso) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet res = null;
        Docenza docenza = null;

        try {
            st = conn.prepareStatement(SQLQuery.FIND_BY_ID_DOCENZA);
            st.setInt(1, idDocente);
            st.setInt(2, idCorso);
            res = st.executeQuery();

            if (res.next()) {
                Corso corso = new Corso(res.getInt("Corso"), res.getString("Titolo"));
                Docente docente = new Docente(res.getInt("Docente"), res.getString("Nome"), res.getString("Cognome"));
                docenza = new Docenza(corso, docente);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(res, st, conn);
        }

        return docenza;
    }

    /**
     * Ritorna la lista delle docenze di un determinato docente.
     *
     * @param idDocente ID del docente
     * @return una lista delle docenze di un determinato docente
     */
    public List<Docenza> findAllByDocente(int idDocente) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet res = null;
        List<Docenza> docenze = new ArrayList<>();

        try {
            st = conn.prepareStatement(SQLQuery.FIND_ALL_BY_DOCENTE_DOCENZA);
            st.setInt(1, idDocente);
            res = st.executeQuery();

            while (res.next()) {
                Corso corso = new Corso(res.getInt("Corso"), res.getString("Titolo"));
                Docente docente = new Docente(res.getInt("Docente"), res.getString("Nome"), res.getString("Cognome"));
                docenze.add(new Docenza(corso, docente));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(res, st, conn);
        }

        return docenze;
    }

    /**
     * Ritorna la lista delle docenze di un determinato corso.
     *
     * @param idCorso ID del corso
     * @return una lista delle docenze di un determinato corso
     */
    public List<Docenza> findAllByCorso(int idCorso) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet res = null;
        List<Docenza> docenze = new ArrayList<>();

        try {
            st = conn.prepareStatement(SQLQuery.FIND_ALL_BY_CORSO_DOCENZA);
            st.setInt(1, idCorso);
            res = st.executeQuery();

            while (res.next()) {
                Corso corso = new Corso(res.getInt("Corso"), res.getString("Titolo"));
                Docente docente = new Docente(res.getInt("Docente"), res.getString("Nome"), res.getString("Cognome"));
                docenze.add(new Docenza(corso, docente));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(res, st, conn);
        }

        return docenze;
    }

    /**
     * Ritorna le informazioni di tutte le docenze
     *
     * @return una lista di tutte le docenze
     */
    public List<Docenza> findAll() {
        Connection conn = connectionManager.getConnection();
        Statement st = null;
        ResultSet res = null;
        List<Docenza> docenze = new ArrayList<>();

        try {
            st = conn.createStatement();
            res = st.executeQuery(SQLQuery.FIND_ALL_DOCENZA);

            while (res.next()) {
                Corso corso = new Corso(res.getInt("Corso"), res.getString("Titolo"));
                Docente docente = new Docente(res.getInt("Docente"), res.getString("Nome"), res.getString("Cognome"));
                docenze.add(new Docenza(corso, docente));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(res, st, conn);
        }

        return docenze;
    }

    /**
     * Cancella la docenza dal database
     *
     * @param idDocente ID del docente
     * @param idCorso ID del corso
     */
    public void deleteByID(int idDocente, int idCorso) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(SQLQuery.DELETE_BY_ID_DOCENZA);
            st.setInt(1, idCorso);
            st.setInt(2, idDocente);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.AUTO_UPDATE_STATO_PRENOTAZIONE);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.UPDATE_PRENOTAZIONI_ATTIVE_BY_DOCENZA);
            st.setInt(1, idDocente);
            st.setInt(2, idCorso);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(rs, st, conn);
        }
    }

    /**
     * Cancella le docenze di un determinato docente dal database, equivalente a
     * DocenteDAO.deleteByID().
     *
     * @param idDocente ID del docente
     */
    public void deleteByDocente(int idDocente) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(SQLQuery.DELETE_BY_DOCENTE_DOCENZA);
            st.setInt(1, idDocente);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.AUTO_UPDATE_STATO_PRENOTAZIONE);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.UPDATE_PRENOTAZIONI_ATTIVE_BY_DOCENTE);
            st.setInt(1, idDocente);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(rs, st, conn);
        }
    }

    /**
     * Cancella le docenze di un determinato corso dal database, equivalente a
     * CorsoDAO.deleteByID().
     *
     * @param idCorso ID del corso
     */
    public void deleteByCorso(int idCorso) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(SQLQuery.DELETE_BY_CORSO_DOCENZA);
            st.setInt(1, idCorso);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.AUTO_UPDATE_STATO_PRENOTAZIONE);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.UPDATE_PRENOTAZIONI_ATTIVE_BY_CORSO);
            st.setInt(1, idCorso);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(rs, st, conn);
        }
    }
}
