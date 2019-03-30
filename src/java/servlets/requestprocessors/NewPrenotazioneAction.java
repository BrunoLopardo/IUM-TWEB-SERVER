package servlets.requestprocessors;

import dao.dto.Prenotazione;
import dao.dto.Ripetizione;
import dao.dto.Corso;
import dao.dto.Docente;
import dao.daos.Dao;
import dao.exceptions.DAOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class NewPrenotazioneAction implements IAction {

    private final String name;

    public NewPrenotazioneAction(String name) {
        this.name = name;
    }

    public NewPrenotazioneAction() {
        this.name = "new_prenotazione";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        
        String username = (String) request.getSession().getAttribute("username");
        int idCorso = 0;
        int idDocente = 0;
        int ora = 0;
        Prenotazione.DayName giorno = null;
        
        if (username == null) {
            SrvUtils.sendStatusCode(response, 3); // Permesso negato
            return;
        }
        
        try {
            idCorso = Integer.parseInt(request.getParameter("idCorso"));
            idDocente = Integer.parseInt(request.getParameter("idDocente"));
            ora = Integer.parseInt(request.getParameter("ora"));
            giorno = Prenotazione.DayName.valueOf(request.getParameter("day"));
        } catch (NumberFormatException e) {
            SrvUtils.sendStatusCode(response, 4); // Non è un numero oppure è null
            return;
        } catch (IllegalArgumentException | NullPointerException e) {
            SrvUtils.sendStatusCode(response, 4); // Day non contiente un valore valido
            return;
        }
        
        if (idCorso == 0 || idDocente == 0 || ora == 0 || giorno == null) {
            SrvUtils.sendStatusCode(response, 1); // informazioni assenti
            return;
        }
        
        Corso corso = dao.getCorsoDAO().findByID(idCorso);
        Docente docente = dao.getDocenteDAO().findByID(idDocente);
         
        Ripetizione ripetizione = new Ripetizione(corso, docente, giorno, ora);
        if (!dao.getRipetizioneDAO().ripetizioneExists(ripetizione)) {
            SrvUtils.sendStatusCode(response, 5); // Ripetizione non presente o non disponibile.
            return;
        }
               
        Prenotazione prenotazione = new Prenotazione(null, corso, docente, dao.getUtenteDAO().findByUsername(username), Prenotazione.Stato.EFFETTUATA, giorno, ora, null);
        try {
            dao.getPrenotazioneDAO().create(prenotazione);
            SrvUtils.sendStatusCode(response, 0); // Success
        } catch (DAOException e) {
            if (e.getErrorCode().equals(DAOException.UNIQUE_VIOLATION)) {
                if (e.getViolatedConstraint().equals(DAOException.PRENOTAZIONE_UTENTE_GIORNO_ORA_KEY)) {
                    SrvUtils.sendStatusCode(response, 6); // Utente con una prenotazione nello stesso giorno e orario.
                } else if (e.getViolatedConstraint().equals(DAOException.PRENOTAZIONE_DOCENTE_GIORNO_ORA_KEY)) {
                    SrvUtils.sendStatusCode(response, 7); // Docente con una prenotazione nello stesso giorno e orario.
                } else { 
                    SrvUtils.sendStatusCode(response, 99);
                    System.err.println(e.getMessage());
                }
            } else {
                SrvUtils.sendStatusCode(response, 99); // Si è verificato un errore
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

}
