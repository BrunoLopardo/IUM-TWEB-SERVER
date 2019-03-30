package servlets.requestprocessors;

import dao.daos.Dao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IAction {

    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao);
    public String getName();
    
}
