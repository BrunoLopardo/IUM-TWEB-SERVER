package dao.daos;

import dao.utils.ConnectionManager;
import dao.dto.Docente;
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

public class DocenteDAO {

    private final ConnectionManager connectionManager;

    public DocenteDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Crea un docente e lo inserisce nel DB, l'ID del docente viene generato
     * dal DB e viene inserito nell'oggetto docente.
     *
     * @param docente il docente da creare
     * @return ID del nuovo docente
     */
    public int create(Docente docente) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        int id = -1;

        try {
            st = conn.prepareStatement(SQLQuery.CREATE_DOCENTE, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, docente.getNome());
            st.setString(2, docente.getCognome());
            st.executeUpdate();

            rs = st.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
                docente.setID(id);
            } else {
                docente.setID(null);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(rs, st, conn);
        }

        return id;
    }

    /**
     * Ritorna le informazioni del docente con l'ID specificato.
     *
     * @param id ID del docente
     * @return docente con l'ID specificato
     */
    public Docente findByID(int id) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet result = null;
        Docente docente = null;

        try {
            st = conn.prepareStatement(SQLQuery.FIND_BY_ID_DOCENTE);
            st.setInt(1, id);
            result = st.executeQuery();

            if (result.next()) {
                docente = new Docente(result.getInt("ID"), result.getString("Nome"), result.getString("Cognome"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }

        return docente;
    }

    /**
     * Ritorna le informazioni del docente con il nome e cognome specificati.
     *
     * @param nome il nome del docente
     * @param cognome il cognome del docente
     * @return docente con il nome e cognome specificati
     */
    public Docente findByNomeAndCognome(String nome, String cognome) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet result = null;
        Docente docente = null;

        try {
            st = conn.prepareStatement(SQLQuery.FIND_BY_NOME_AND_COGNOME_DOCENTE);
            st.setString(1, nome);
            st.setString(2, cognome);
            result = st.executeQuery();

            if (result.next()) {
                docente = new Docente(result.getInt("ID"), result.getString("Nome"), result.getString("Cognome"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }

        return docente;
    }

    /**
     * Ritorna le informazioni di tutti i docenti
     *
     * @return una lista di tutti i docenti
     */
    public List<Docente> findAll() {
        Connection conn = connectionManager.getConnection();
        Statement st = null;
        ResultSet result = null;
        List<Docente> docenti = new ArrayList<>();

        try {
            st = conn.createStatement();
            result = st.executeQuery(SQLQuery.FIND_ALL_DOCENTE);

            while (result.next()) {
                docenti.add(new Docente(result.getInt("ID"), result.getString("Nome"), result.getString("Cognome")));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }

        return docenti;
    }
    
    /**
     * Ritorna le informazioni di tutti i docenti attivi
     * 
     * @return un insieme di docenti attivi (HashSet per ragioni di performance)
     */
    public Set<Docente> findAllActive() {
        Connection conn = connectionManager.getConnection();
        Statement st = null;
        ResultSet result = null;
        Set<Docente> corsi = new HashSet<>();

        try {
            st = conn.createStatement();
            result = st.executeQuery(SQLQuery.FIND_ALL_ACTIVE_DOCENTE);

            while (result.next()) {
                corsi.add(new Docente(result.getInt("ID"), result.getString("Nome"), result.getString("Cognome")));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }

        return corsi;
    }

    /**
     * Setta tutti i dati del docente eccetto l'ID, il docente da modificare è
     * scelto in base all'ID.
     *
     * @param docente oggetto docente con i dati modificati
     */
    public void update(Docente docente) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(SQLQuery.UPDATE_DOCENTE);
            st.setString(1, docente.getNome());
            st.setString(2, docente.getCognome());
            st.setInt(3, docente.getID());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(rs, st, conn);
        }
    }

    /**
     * Cancella il docente SENZA rimuoverlo dal DB, ovvero rimuove tutte le
     * docenze di quel docente.
     *
     * @param id ID del docente da eliminare
     */
    public void deleteByID(int id) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet res = null;

        try {
            st = conn.prepareStatement(SQLQuery.DELETE_BY_DOCENTE_DOCENZA);
            st.setInt(1, id);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.AUTO_UPDATE_STATO_PRENOTAZIONE);
            st.executeUpdate();
                        
            st = conn.prepareStatement(SQLQuery.UPDATE_PRENOTAZIONI_ATTIVE_BY_DOCENTE);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(res, st, conn);
        }
    }

    /**
     * Cancella il docente SENZA rimuoverlo dal DB, ovvero rimuove tutte le
     * docenze di quel docente.
     *
     * @param nome nome del docente da eliminare
     * @param cognome cognome del docente da eliminare
     */
    public void deleteByNomeAndCognome(String nome, String cognome) {
        Docente docente = findByNomeAndCognome(nome, cognome);
        if (docente != null) {
            deleteByID(docente.getID());
        }
    }
}
