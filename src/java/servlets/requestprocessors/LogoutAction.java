package servlets.requestprocessors;

import dao.daos.Dao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutAction implements IAction {
    
    private final String name;

    public LogoutAction(String name) {
        this.name = name;
    }

    public LogoutAction() {
        this.name = "logout";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        request.getSession().invalidate();
    }

    @Override
    public String getName() {
        return name;
    }
    
}
