package servlets.requestprocessors;

import dao.daos.Dao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminAuthActionDecorator implements IAction {
    
    private final IAction payload;

    public AdminAuthActionDecorator(IAction payload) {
        this.payload = payload;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        Boolean isAdmin = (Boolean) request.getSession().getAttribute("isAdmin");
        
        if (isAdmin == null || !isAdmin) {
            response.setStatus(401); // 401 Unauthorized
            return;
        }
        
        payload.execute(request, response, dao);
    }

    @Override
    public String getName() {
        return payload.getName();
    }
}
