package servlets.requestprocessors;

import dao.daos.Dao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultAction implements IAction {
    
    private final String name = "default";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {}

    @Override
    public String getName() {
        return name;
    }
    
}
