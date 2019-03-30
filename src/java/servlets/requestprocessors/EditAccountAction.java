package servlets.requestprocessors;

import dao.dto.Utente;
import dao.daos.Dao;
import dao.exceptions.DAOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class EditAccountAction implements IAction {

    private final String name;

    public EditAccountAction(String name) {
        this.name = name;
    }

    public EditAccountAction() {
        this.name = "edit_account";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        String oldUsername = (String) request.getSession().getAttribute("username");
        String oldPassword = request.getParameter("oldPassword");
        String newUsername = request.getParameter("newUsername");
        String newPassword = request.getParameter("newPassword");

        if(oldUsername == null){
            SrvUtils.sendStatusCode(response, 5); // L'utente deve essere loggato
            return;
        }
        
        Utente utente = dao.getUtenteDAO().findByUsername(oldUsername);

        boolean condition = (oldPassword == null || newUsername == null || newPassword == null);
        condition = condition || oldPassword.isEmpty() || newUsername.isEmpty() || newPassword.isEmpty();
    
        if(condition){
            SrvUtils.sendStatusCode(response, 1); // Username e/o password non possono essere vuoti.
            return;
        }
       
        if(!(utente.getPassword().equals(oldPassword))){
            SrvUtils.sendStatusCode(response, 4);// La vecchia password non corrisponde a quella in uso
            return;
        }
          
        try {
            utente.setUsername(newUsername);
            utente.setPassword(newPassword);
            dao.getUtenteDAO().update(utente);
            request.getSession().setAttribute("username", newUsername);
            SrvUtils.sendStatusCode(response, 0); // Success
        } catch (DAOException e) {
            switch(e.getErrorCode()){
                case DAOException.UNIQUE_VIOLATION:
                    SrvUtils.sendStatusCode(response, 2); // Lo username è già in uso da un altro utente
                    break;
                case DAOException.STRING_DATA_RIGHT_TRUNCATION:
                    SrvUtils.sendStatusCode(response, 3); // Username e/o password troppo lunghi
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
