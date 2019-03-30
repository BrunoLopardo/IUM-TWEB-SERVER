package dao.daos;

import dao.utils.ConnectionManager;
import dao.dto.Corso;
import dao.dto.Docente;
import dao.dto.Prenotazione;
import dao.utils.SQLQuery;
import dao.dto.Utente;
import dao.exceptions.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrenotazioneDAO {
    
    private final ConnectionManager connectionManager;
    
    public PrenotazioneDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Crea una prenotazione e la inserisce nel DB, l'ID della prenotazione
     * viene generato dal DB e viene inserito nell'oggetto prenotazione.
     *
     * @param prenotazione la prenotazione da creare
     * @return ID della nuova prenotazione
     */
    public int create(Prenotazione prenotazione) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        int id = -1;
        
        try {
            st = conn.prepareStatement(SQLQuery.AUTO_UPDATE_STATO_PRENOTAZIONE);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.CREATE_PRENOTAZIONE, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, prenotazione.getCorso().getID());
            st.setInt(2, prenotazione.getDocente().getID());
            st.setInt(3, prenotazione.getUtente().getID());
            st.setObject(4, prenotazione.getGiorno(), Types.OTHER);
            st.setInt(5, prenotazione.getOraInizio());
            st.executeUpdate();
            
            rs = st.getGeneratedKeys();
            
            if (rs.next()) {
                id = rs.getInt(1);
                prenotazione.setID(id);
            } else {
                prenotazione.setID(null);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(rs, st, conn);
        }
        
        return id;
    }

    /**
     * Ritorna le informazioni della prenotazione con l'ID specificato.
     *
     * @param id ID della prenotazione
     * @return prenotazione con l'ID specificato
     */
    public Prenotazione findByID(int id) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet result = null;
        Prenotazione prenotazione = null;
        
        try {
            st = conn.prepareStatement(SQLQuery.AUTO_UPDATE_STATO_PRENOTAZIONE);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.FIND_BY_ID_PRENOTAZIONE);
            st.setInt(1, id);
            result = st.executeQuery();
            
            if (result.next()) {
                Corso corso = new Corso(result.getInt("Corso_ID"), result.getString("Titolo"));
                Docente docente = new Docente(result.getInt("Docente_ID"), result.getString("Nome"),
                        result.getString("Cognome"));
                Utente utente = new Utente(result.getInt("Utente_ID"), result.getString("Username"),
                        result.getString("Password"), result.getBoolean("isAdmin"));
                prenotazione = new Prenotazione(result.getInt("Prenotazione_ID"), corso, docente, utente,
                        Prenotazione.Stato.valueOf(result.getString("Stato").toUpperCase()),
                        Prenotazione.DayName.valueOf(result.getString("Giorno").toUpperCase()),
                        result.getInt("Ora_inizio"),
                        result.getDouble("Created_at"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }
        
        return prenotazione;
    }

    /**
     * Ritorna le informazioni delle prenotazioni di un determinato corso.
     *
     * @param idCorso ID del corso
     * @return lista di prenotazioni del corso specificato
     */
    public List<Prenotazione> findAllByCorso(int idCorso) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet result = null;
        List<Prenotazione> prenotazioni = new ArrayList<>();
        
        try {
            st = conn.prepareStatement(SQLQuery.AUTO_UPDATE_STATO_PRENOTAZIONE);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.FIND_ALL_BY_CORSO_PRENOTAZIONE);
            st.setInt(1, idCorso);
            result = st.executeQuery();
            
            while (result.next()) {
                Corso corso = new Corso(result.getInt("Corso_ID"), result.getString("Titolo"));
                Docente docente = new Docente(result.getInt("Docente_ID"), result.getString("Nome"),
                        result.getString("Cognome"));
                Utente utente = new Utente(result.getInt("Utente_ID"), result.getString("Username"),
                        result.getString("Password"), result.getBoolean("isAdmin"));
                
                Prenotazione prenotazione = new Prenotazione(result.getInt("Prenotazione_ID"), corso, docente, utente,
                        Prenotazione.Stato.valueOf(result.getString("Stato").toUpperCase()),
                        Prenotazione.DayName.valueOf(result.getString("Giorno").toUpperCase()),
                        result.getInt("Ora_inizio"),
                        result.getDouble("Created_at"));
                
                prenotazioni.add(prenotazione);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }
        
        return prenotazioni;
    }

    /**
     * Ritorna le informazioni delle prenotazioni di un determinato docente.
     *
     * @param idDocente ID del docente
     * @return lista di prenotazioni del docente specificato
     */
    public List<Prenotazione> findAllByDocente(int idDocente) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet result = null;
        List<Prenotazione> prenotazioni = new ArrayList<>();
        
        try {
            st = conn.prepareStatement(SQLQuery.AUTO_UPDATE_STATO_PRENOTAZIONE);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.FIND_ALL_BY_DOCENTE_PRENOTAZIONE);
            st.setInt(1, idDocente);
            result = st.executeQuery();
            
            while (result.next()) {
                Corso corso = new Corso(result.getInt("Corso_ID"), result.getString("Titolo"));
                Docente docente = new Docente(result.getInt("Docente_ID"), result.getString("Nome"),
                        result.getString("Cognome"));
                Utente utente = new Utente(result.getInt("Utente_ID"), result.getString("Username"),
                        result.getString("Password"), result.getBoolean("isAdmin"));
                
                Prenotazione prenotazione = new Prenotazione(result.getInt("Prenotazione_ID"), corso, docente, utente,
                        Prenotazione.Stato.valueOf(result.getString("Stato").toUpperCase()),
                        Prenotazione.DayName.valueOf(result.getString("Giorno").toUpperCase()),
                        result.getInt("Ora_inizio"),
                        result.getDouble("Created_at"));
                
                prenotazioni.add(prenotazione);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }
        
        return prenotazioni;
    }

    /**
     * Ritorna le informazioni delle prenotazioni di un determinato utente.
     *
     * @param idUtente ID dell'utente
     * @return lista di prenotazioni dell'utente specificato
     */
    public List<Prenotazione> findAllByUtente(int idUtente) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet res = null;
        List<Prenotazione> prenotazioni = new ArrayList<>();
        
        try {
            st = conn.prepareStatement(SQLQuery.AUTO_UPDATE_STATO_PRENOTAZIONE);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.FIND_ALL_BY_UTENTE_PRENOTAZIONE);
            st.setInt(1, idUtente);
            res = st.executeQuery();
            
            while (res.next()) {
                Corso corso = new Corso(res.getInt("Corso_ID"), res.getString("Titolo"));
                Docente docente = new Docente(res.getInt("Docente_ID"), res.getString("Nome"), res.getString("Cognome"));
                Utente utente = new Utente(res.getInt("Utente_ID"), res.getString("Username"),
                        res.getString("Password"), res.getBoolean("isAdmin"));
                
                Prenotazione prenotazione = new Prenotazione(res.getInt("Prenotazione_ID"), corso, docente, utente,
                        Prenotazione.Stato.valueOf(res.getString("Stato").toUpperCase()),
                        Prenotazione.DayName.valueOf(res.getString("Giorno").toUpperCase()),
                        res.getInt("Ora_inizio"),
                        res.getDouble("Created_at"));
                
                prenotazioni.add(prenotazione);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(res, st, conn);
        }
        
        return prenotazioni;
    }

    /**
     * Ritorna le informazioni delle prenotazioni in un determinato stato.
     *
     * @param stato stato della prenotazione
     * @return lista delle prenotazioni in un determinato stato
     */
    public List<Prenotazione> findAllByStato(Prenotazione.Stato stato) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet result = null;
        List<Prenotazione> prenotazioni = new ArrayList<>();
        
        try {
            st = conn.prepareStatement(SQLQuery.AUTO_UPDATE_STATO_PRENOTAZIONE);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.FIND_ALL_BY_STATO_PRENOTAZIONE);
            st.setObject(1, stato, Types.OTHER);
            result = st.executeQuery();
            
            while (result.next()) {
                Corso corso = new Corso(result.getInt("Corso_ID"), result.getString("Titolo"));
                Docente docente = new Docente(result.getInt("Docente_ID"), result.getString("Nome"),
                        result.getString("Cognome"));
                Utente utente = new Utente(result.getInt("Utente_ID"), result.getString("Username"),
                        result.getString("Password"), result.getBoolean("isAdmin"));
                
                Prenotazione prenotazione = new Prenotazione(result.getInt("Prenotazione_ID"), corso, docente, utente,
                        Prenotazione.Stato.valueOf(result.getString("Stato").toUpperCase()),
                        Prenotazione.DayName.valueOf(result.getString("Giorno").toUpperCase()),
                        result.getInt("Ora_inizio"),
                        result.getDouble("Created_at"));
                
                prenotazioni.add(prenotazione);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }
        
        return prenotazioni;
    }

    /**
     * Ritorna le informazioni di tutte le prenotazioni.
     *
     * @return una lista di tutte le prenotazioni
     */
    public List<Prenotazione> findAll() {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet result = null;
        List<Prenotazione> prenotazioni = new ArrayList<>();
        
        try {
            st = conn.prepareStatement(SQLQuery.AUTO_UPDATE_STATO_PRENOTAZIONE);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.FIND_ALL_PRENOTAZIONE);
            result = st.executeQuery();
            
            while (result.next()) {
                Corso corso = new Corso(result.getInt("Corso_ID"), result.getString("Titolo"));
                Docente docente = new Docente(result.getInt("Docente_ID"), result.getString("Nome"),
                        result.getString("Cognome"));
                Utente utente = new Utente(result.getInt("Utente_ID"), result.getString("Username"),
                        result.getString("Password"), result.getBoolean("isAdmin"));
                
                Prenotazione prenotazione = new Prenotazione(result.getInt("Prenotazione_ID"), corso, docente, utente,
                        Prenotazione.Stato.valueOf(result.getString("Stato").toUpperCase()),
                        Prenotazione.DayName.valueOf(result.getString("Giorno").toUpperCase()),
                        result.getInt("Ora_inizio"),
                        result.getDouble("Created_at"));
                
                prenotazioni.add(prenotazione);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }
        
        return prenotazioni;
    }
    
    /**
     * Ritorna le informazioni di tutte le prenotazioni raggruppate per utente.
     *
     * @return una HashMap con tutte le prenotazioni raggruppate per utente
     */
    public Map<Utente, List<Prenotazione>> findAllGroupByUtente() {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet result = null;
        Map<Utente, List<Prenotazione>> prenotazioni = new HashMap<>();
        
        try {
            st = conn.prepareStatement(SQLQuery.AUTO_UPDATE_STATO_PRENOTAZIONE);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.FIND_ALL_PRENOTAZIONE);
            result = st.executeQuery();
            
            while (result.next()) {
                Corso corso = new Corso(result.getInt("Corso_ID"), result.getString("Titolo"));
                Docente docente = new Docente(result.getInt("Docente_ID"), result.getString("Nome"),
                        result.getString("Cognome"));
                Utente utente = new Utente(result.getInt("Utente_ID"), result.getString("Username"),
                        result.getString("Password"), result.getBoolean("isAdmin"));
                
                Prenotazione prenotazione = new Prenotazione(result.getInt("Prenotazione_ID"), corso, docente, utente,
                        Prenotazione.Stato.valueOf(result.getString("Stato").toUpperCase()),
                        Prenotazione.DayName.valueOf(result.getString("Giorno").toUpperCase()),
                        result.getInt("Ora_inizio"),
                        result.getDouble("Created_at"));
                
                if (prenotazioni.containsKey(utente)) {
                    prenotazioni.get(utente).add(prenotazione);
                } else {
                    List<Prenotazione> prenotazioniUtente = new ArrayList<>();
                    prenotazioniUtente.add(prenotazione);
                    prenotazioni.put(utente, prenotazioniUtente);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }
        
        return prenotazioni;
    }

    /**
     * Modifica lo stato di una prenotazione.
     *
     * @param id ID della prenotazione da modificare
     * @param nuovoStato il nuovo stato della prenotazione
     */
    public void updateStato(int id, Prenotazione.Stato nuovoStato) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            st = conn.prepareStatement(SQLQuery.UPDATE_STATO_PRENOTAZIONE);
            st.setObject(1, nuovoStato, Types.OTHER);
            st.setInt(2, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(rs, st, conn);
        }
    }

    /**
     * Imposta lo stato della prenotazione a 'Disdetta'.
     *
     * @param id ID della prenotazione da cancellare
     */
    public void deleteByID(int id) {
        updateStato(id, Prenotazione.Stato.DISDETTA);
    }
}
