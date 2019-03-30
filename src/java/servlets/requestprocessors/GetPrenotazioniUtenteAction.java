package servlets.requestprocessors;

import dao.daos.Dao;
import dao.dto.Prenotazione;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class GetPrenotazioniUtenteAction implements IAction{
    private final String name;

    public GetPrenotazioniUtenteAction(String name) {
        this.name = name;
    }

    public GetPrenotazioniUtenteAction() {
        this.name = "get_prenotazioni_utente";
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        String username = (String) request.getSession().getAttribute("username");
        
        if(username == null){
            SrvUtils.sendStatusCode(response, 1);
            return;
        }
        
        int idUtente = dao.getUtenteDAO().findByUsername(username).getID();
        List<Prenotazione> prenotazioniUtente = dao.getPrenotazioneDAO().findAllByUtente(idUtente);
       
        for(Prenotazione prenotazione : prenotazioniUtente){
            prenotazione.getUtente().setPassword(null);
        }
        
        SrvUtils.sendJsonObject(response, prenotazioniUtente);
    }

    @Override
    public String getName() {
        return name;
    } 
}
