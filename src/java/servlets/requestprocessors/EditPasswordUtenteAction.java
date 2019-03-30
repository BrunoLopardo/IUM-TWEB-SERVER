package servlets.requestprocessors;

import dao.daos.Dao;
import dao.dto.Utente;
import dao.exceptions.DAOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class EditPasswordUtenteAction implements IAction {

    private final String name;

    public EditPasswordUtenteAction(String name) {
        this.name = name;
    }

    public EditPasswordUtenteAction() {
        this.name = "edit_password_utente";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        int id = 0;
        String newPassword = request.getParameter("newPassword");

        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            SrvUtils.sendStatusCode(response, 1); // Non è un numero oppure è null
            return;
        }
        
        if(newPassword == null || newPassword.isEmpty()){
            SrvUtils.sendStatusCode(response, 2); // campo vuoto
            return;
        }
        
        Utente utente = dao.getUtenteDAO().findByID(id);
        
        if(utente == null) {
            SrvUtils.sendStatusCode(response, 3); // utente inesistente
            return;
        }
        
        try {
            utente.setPassword(newPassword);
            dao.getUtenteDAO().update(utente);
            SrvUtils.sendStatusCode(response, 0); // Success
        } catch(DAOException e) {
            switch (e.getErrorCode()) {
                case DAOException.STRING_DATA_RIGHT_TRUNCATION:
                    SrvUtils.sendStatusCode(response, 4); // Password troppo lunga
                    break;
                default:
                    SrvUtils.sendStatusCode(response, 99); // Unknown error
                    System.err.println(e.getMessage());
                    break;
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

}
