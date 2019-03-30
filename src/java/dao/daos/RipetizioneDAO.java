package dao.daos;

import dao.utils.ConnectionManager;
import dao.dto.Corso;
import dao.dto.Docente;
import dao.dto.Prenotazione;
import dao.dto.Ripetizione;
import dao.utils.SQLQuery;
import dao.exceptions.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class RipetizioneDAO {

    private final ConnectionManager connectionManager;

    public RipetizioneDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Ritorna le informazioni su tutte le ripetizioni disponibili di un
     * determinato docente e corso.
     *
     * @param idDocente ID del docente, se null seleziona tutti i docenti
     * @param idCorso ID del corso, se null seleziona tutti i corsi
     * @return una lista delle ripetizioni disponibili che rispettano il filtro
     */
    public List<Ripetizione> findAllByDocenteAndCorsoAndStatoDisponibile(Integer idDocente, Integer idCorso) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet result = null;
        List<Ripetizione> ripetizioni = new ArrayList<>();

        try {
            st = conn.prepareStatement(SQLQuery.AUTO_UPDATE_STATO_PRENOTAZIONE);
            st.executeUpdate();
            
            if (idCorso != null && idDocente == null) {
                st = conn.prepareStatement(SQLQuery.FIND_ALL_BY_CORSO_AND_STATO_DISPONIBILE_RIPETIZIONE);
                st.setInt(1, idCorso);
                result = st.executeQuery();
            } else if (idCorso == null && idDocente != null) {
                st = conn.prepareStatement(SQLQuery.FIND_ALL_BY_DOCENTE_AND_STATO_DISPONIBILE_RIPETIZIONE);
                st.setInt(1, idDocente);
                result = st.executeQuery();
            } else if (idCorso == null && idDocente == null) {
                st = conn.prepareStatement(SQLQuery.FIND_ALL_BY_STATO_DISPONIBILE_RIPETIZIONE);
                result = st.executeQuery();
            } else {
                st = conn.prepareStatement(SQLQuery.FIND_ALL_BY_DOCENTE_AND_CORSO_AND_STATO_DISPONIBILE_RIPETIZIONE);
                st.setInt(1, idCorso);
                st.setInt(2, idDocente);
                result = st.executeQuery();
            }

            while (result.next()) {
                Corso corso = new Corso(result.getInt("Corso_ID"), result.getString("Titolo"));
                Docente docente = new Docente(result.getInt("Docente_ID"), result.getString("Nome"),
                        result.getString("Cognome"));
                Ripetizione ripetizione = new Ripetizione(corso, docente,
                        Prenotazione.DayName.valueOf(result.getString("Giorno").toUpperCase()), result.getInt("Ora_inizio"));
                ripetizioni.add(ripetizione);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }

        return ripetizioni;
    }

    /**
     * Ritorna le informazioni su tutte le ripetizioni disponibili, equivalente
     * a findAllByDocenteAndCorsoAndStatoDisponibile(null, null).
     *
     * @return una lista di tutte le ripetizioni disponibili
     */
    public List<Ripetizione> findAllByStatoDisponibile() {
        return findAllByDocenteAndCorsoAndStatoDisponibile(null, null);
    }

    /**
     * Ritorna le informazioni su tutte le ripetizioni
     *
     * @return una lista di tutte le ripetizioni
     */
    public List<Ripetizione> findAll() {
        Connection conn = connectionManager.getConnection();
        Statement st = null;
        ResultSet res = null;
        List<Ripetizione> result = new ArrayList<>();

        try {
            st = conn.createStatement();
            res = st.executeQuery(SQLQuery.FIND_ALL_RIPETIZIONE);

            while (res.next()) {
                Corso corso = new Corso(res.getInt("Corso_ID"), res.getString("Titolo"));
                Docente docente = new Docente(res.getInt("Docente_ID"), res.getString("Nome"), res.getString("Cognome"));
                Ripetizione ripetizione = new Ripetizione(corso, docente, Prenotazione.DayName.valueOf(res.getString("Giorno").toUpperCase()), res.getInt("Ora_inizio"));
                result.add(ripetizione);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(res, st, conn);
        }

        return result;
    }
    
    /**
     * Ritorna true se la ripetizione esiste ed è disponibile, false altrimenti.
     *
     * @return ritorna true se la ripetizione esiste ed è disponibile, false altrimenti.
     */
    public boolean ripetizioneExists(Ripetizione ripetizione) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement st = null;
        ResultSet result = null;
        boolean stato = false;
        
        try {
            st = conn.prepareStatement(SQLQuery.AUTO_UPDATE_STATO_PRENOTAZIONE);
            st.executeUpdate();
            
            st = conn.prepareStatement(SQLQuery.RIPETIZIONE_EXISTS);
            st.setInt(1, ripetizione.getCorso().getID());
            st.setInt(2, ripetizione.getDocente().getID());
            st.setObject(3, ripetizione.getGiorno(), Types.OTHER);
            st.setInt(4, ripetizione.getOraInizio());
            result = st.executeQuery();

            if (result.next()) {
                stato = true;
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            ConnectionManager.closeAll(result, st, conn);
        }

        return stato;
    }
}
