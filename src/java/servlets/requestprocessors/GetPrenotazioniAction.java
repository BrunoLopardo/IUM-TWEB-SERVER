package servlets.requestprocessors;

import dao.dto.Prenotazione;
import dao.dto.Utente;
import dao.daos.Dao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class GetPrenotazioniAction implements IAction {
    
    private final String name;

    public GetPrenotazioniAction(String name) {
        this.name = name;
    }

    public GetPrenotazioniAction() {
        this.name = "get_prenotazioni";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        List<PrenotazioneRich> prenotazioni = new ArrayList<>();
        
        Map<Utente, List<Prenotazione>> prenotazioniRaggruppate = dao.getPrenotazioneDAO().findAllGroupByUtente();
        
        for(Utente utente : prenotazioniRaggruppate.keySet()) {
            prenotazioni.add(new PrenotazioneRich(utente.getID(), utente.getUsername(), prenotazioniRaggruppate.get(utente)));
        }
        
        SrvUtils.sendJsonObject(response, prenotazioni);
    }

    @Override
    public String getName() {
        return name;
    }
    
    private class PrenotazioneRich {
        public final int id;
        public final String username;
        public final List<Prenotazione> prenotazioni;
        
        public PrenotazioneRich(Integer id, String username, List<Prenotazione> prenotazioni) {
            this.id = id;
            this. username = username;
            this.prenotazioni = prenotazioni;
        }
    }
}
