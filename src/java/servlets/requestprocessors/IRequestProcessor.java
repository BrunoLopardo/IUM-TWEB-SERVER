package servlets.requestprocessors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRequestProcessor {
    public void processRequest(HttpServletRequest request, HttpServletResponse response);
    public void registerAction(IAction action);
}
