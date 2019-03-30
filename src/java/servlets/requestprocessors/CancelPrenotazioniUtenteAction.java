package servlets.requestprocessors;

import dao.daos.Dao;
import dao.dto.Prenotazione;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class CancelPrenotazioniUtenteAction implements IAction {

    private final String name;

    public CancelPrenotazioniUtenteAction(String name) {
        this.name = name;
    }

    public CancelPrenotazioniUtenteAction() {
        this.name = "cancel_prenotazioni_utente";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        String username = (String) request.getSession().getAttribute("username"); // prende lo username dalla sessione
        int idPrenotazione = 0;
        
        try {
            idPrenotazione = Integer.parseInt(request.getParameter("idPrenotazione"));
        } catch(NumberFormatException ex) {
            SrvUtils.sendStatusCode(response, 1); // Non è un numero oppure null
            return;
        }   
            
        Prenotazione prenotazione = dao.getPrenotazioneDAO().findByID(idPrenotazione);
        
        if(prenotazione == null){
            SrvUtils.sendStatusCode(response, 2); // Prenotazione inesistente
            return;
        }
        
        String usernamePrenotazione = prenotazione.getUtente().getUsername();

        if (!(usernamePrenotazione.equals(username))) {
            SrvUtils.sendStatusCode(response, 3); // La prenotazione non appartiene all'utente loggato
            return;
        }
        
        if(!(prenotazione.getStato().equals(Prenotazione.Stato.ATTIVA))){
            SrvUtils.sendStatusCode(response, 4); // Impossibile disdire una prenotazione non attiva
            return;
        }
        
        dao.getPrenotazioneDAO().deleteByID(idPrenotazione); // disdice la prenotazione
        SrvUtils.sendStatusCode(response, 0);
    }

    @Override
    public String getName() {
        return name;
    }

}
