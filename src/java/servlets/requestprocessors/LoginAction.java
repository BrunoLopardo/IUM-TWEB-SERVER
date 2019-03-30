package servlets.requestprocessors;

import dao.daos.Dao;
import dao.dto.Utente;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class LoginAction implements IAction {
    
    private final String name;

    public LoginAction(String name) {
        this.name = name;
    }

    public LoginAction() {
        this.name = "login";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Utente utente = dao.getUtenteDAO().findByUsername(username);
        
        request.getSession().invalidate();

        String res = "";
        
        if (utente != null && utente.getPassword().equals(password)) {
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("isAdmin", utente.isIsAdmin());

            res = "{ "
                    + "\"loggedIn\": true, "
                    + "\"username\": \"" + request.getSession().getAttribute("username") +  "\", "
                    + "\"isAdmin\": " + request.getSession().getAttribute("isAdmin") + " }";

        } else {
            res = "{ \"loggedIn\": false }";
        }
        
        SrvUtils.sendResponse(response, res);
    }

    @Override
    public String getName() {
        return name;
    }
}
