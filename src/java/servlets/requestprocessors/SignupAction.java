package servlets.requestprocessors;

import dao.dto.Utente;
import dao.daos.Dao;
import dao.exceptions.DAOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class SignupAction implements IAction {
        
    private final String name;

    public SignupAction(String name) {
        this.name = name;
    }

    public SignupAction() {
        this.name = "signup";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            SrvUtils.sendStatusCode(response, 1); // Username e password non possono essere vuoti
            return;
        }
        
        Utente newUtente = new Utente(null, username, password, false);
        
        try {
            dao.getUtenteDAO().create(newUtente);
            request.getSession().setAttribute("username", newUtente.getUsername());
            request.getSession().setAttribute("isAdmin", newUtente.isIsAdmin());
            SrvUtils.sendStatusCode(response, 0); // Success
        } catch (DAOException e) {            
            switch (e.getErrorCode()) {
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
