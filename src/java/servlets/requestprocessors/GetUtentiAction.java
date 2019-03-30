package servlets.requestprocessors;

import dao.daos.Dao;
import dao.dto.Utente;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class GetUtentiAction implements IAction {

    private final String name;
    
    public GetUtentiAction(String name){
        this.name = name;
    }
    
    public GetUtentiAction() {
        this.name = "get_utenti";
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        List<Utente> listUtenti = dao.getUtenteDAO().findAll();
        
        if(listUtenti == null){
            SrvUtils.sendStatusCode(response, 1);
            return;
        }
        
        SrvUtils.sendJsonObject(response, listUtenti);
    }

    @Override
    public String getName() {
       return name;
    }   
}
