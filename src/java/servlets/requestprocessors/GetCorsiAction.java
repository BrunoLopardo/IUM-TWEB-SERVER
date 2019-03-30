package servlets.requestprocessors;

import dao.dto.Corso;
import dao.daos.Dao;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class GetCorsiAction implements IAction {
    
    private final String name;

    public GetCorsiAction(String name) {
        this.name = name;
    }

    public GetCorsiAction() {
        this.name = "get_corsi";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        List<Corso> corsi = dao.getCorsoDAO().findAll();
        Set<Corso> corsiAttivi = dao.getCorsoDAO().findAllActive();
        
        List<CorsoStato> corsiWithStatus = new ArrayList<>();
        
        for (Corso corso : corsi) {
           corsiWithStatus.add(new CorsoStato(corso.getID(), corso.getTitolo(), corsiAttivi.contains(corso)));
        }
        
        SrvUtils.sendJsonObject(response, corsiWithStatus);
    }

    @Override
    public String getName() {
        return name;
    }
    
    private class CorsoStato {

        public final int id;
        public final String titolo;
        public final boolean stato;

        public CorsoStato(int id, String titolo, boolean stato) {
            this.id = id;
            this.titolo = titolo;
            this.stato = stato;
        }
    }
}
