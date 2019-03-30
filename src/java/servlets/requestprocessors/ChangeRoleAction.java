package servlets.requestprocessors;

import dao.daos.Dao;
import dao.dto.Utente;
import dao.exceptions.DAOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class ChangeRoleAction implements IAction {
    
    private final String name;

    public ChangeRoleAction(String name) {
        this.name = name;
    }

    public ChangeRoleAction() {
        this.name = "change_role_utente";
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        int id = 0;

        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            SrvUtils.sendStatusCode(response, 1); // Non è un numero oppure è null
            return;
        }
        
        Utente utente = dao.getUtenteDAO().findByID(id);
        
        if(utente == null) {
            SrvUtils.sendStatusCode(response, 2); // utente inesistente
            return;
        }
        
        try {
            utente.setIsAdmin(!utente.isIsAdmin()); // inverte il ruolo dell'utente
            dao.getUtenteDAO().update(utente);
            SrvUtils.sendStatusCode(response, 0); // Success
        } catch(DAOException e) {
            SrvUtils.sendStatusCode(response, 99); // Unknown error
            System.err.println(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return name;
    }
    
}
