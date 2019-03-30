package servlets.requestprocessors;

import dao.daos.Dao;
import dao.dto.Utente;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class DeleteAccountAction implements IAction {

    private final String name;

    public DeleteAccountAction(String name) {
        this.name = name;
    }

    public DeleteAccountAction() {
        this.name = "delete_account";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        String username = (String) request.getSession().getAttribute("username");
        String password = request.getParameter("password");
        
        if(username == null){
            SrvUtils.sendStatusCode(response, 3); // L'utente deve essere loggato
            return;
        }
        
        Utente utente = dao.getUtenteDAO().findByUsername(username);
       
        if (password == null || password.isEmpty()) {
            SrvUtils.sendStatusCode(response, 1); // La password non può essere vuota.
            return;
        } 
        
        if(!(utente.getPassword().equals(password))){
            SrvUtils.sendStatusCode(response, 2);// Password errata
            return;
        }
        
        dao.getUtenteDAO().deleteByUsername(username);
        request.getSession().invalidate();
        SrvUtils.sendStatusCode(response, 0); // Success
    }

    @Override
    public String getName() {
        return name;
    }

}
