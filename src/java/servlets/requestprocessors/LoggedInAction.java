package servlets.requestprocessors;

import dao.daos.Dao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class LoggedInAction implements IAction {
    
    private final String name;

    public LoggedInAction(String name) {
        this.name = name;
    }

    public LoggedInAction() {
        this.name = "logged_in";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        
        String res = "";
        if (request.getSession().getAttribute("username") != null) {
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
