package servlets.requestprocessors;

import dao.daos.Dao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class DeleteUtenteAction implements IAction {

    private final String name;
    
    public DeleteUtenteAction(String name) {
        this.name = name;
    }
    
    public DeleteUtenteAction() {
        this.name = "delete_utente";
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        int id = 0;
        
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch(NumberFormatException e) {
            SrvUtils.sendStatusCode(response, 1); // Non è un numero oppure è null
            return;
        }
        
        if (dao.getUtenteDAO().findByID(id) != null) {
            dao.getUtenteDAO().deleteByID(id);
            SrvUtils.sendStatusCode(response, 0); // Success
        } else {
            SrvUtils.sendStatusCode(response, 2); // Utente inesistente
        }
    }

    @Override
    public String getName() {
        return name;
    }
    
}
