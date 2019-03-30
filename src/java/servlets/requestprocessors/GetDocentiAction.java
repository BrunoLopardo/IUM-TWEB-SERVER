package servlets.requestprocessors;

import dao.dto.Docente;
import dao.daos.Dao;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class GetDocentiAction implements IAction {
    
    private final String name;

    public GetDocentiAction(String name) {
        this.name = name;
    }

    public GetDocentiAction() {
        this.name = "get_docenti";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) { 
        List<Docente> docenti = dao.getDocenteDAO().findAll();
        Set<Docente> docentiAttivi = dao.getDocenteDAO().findAllActive();
        
        List<DocenteStato> docentiWithStatus = new ArrayList<>();
        
        for (Docente docente : docenti) {
           docentiWithStatus.add(new DocenteStato(docente.getID(), docente.getNome(), docente.getCognome(), docentiAttivi.contains(docente)));
        }
        
        SrvUtils.sendJsonObject(response, docentiWithStatus);
    }

    @Override
    public String getName() {
        return name;
    }
    
    private class DocenteStato {

        public final int id;
        public final String nome;
        public final String cognome;
        public final boolean stato;

        public DocenteStato(int id, String nome, String cognome, boolean stato) {
            this.id = id;
            this.nome = nome;
            this.cognome = cognome;
            this.stato = stato;
        }
    }
}
