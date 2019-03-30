package servlets.requestprocessors;

import dao.daos.Dao;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainRequestProcessor implements IRequestProcessor {

    private Dao dao;
    private HashMap<String, IAction> actions = new HashMap<>();
    private IAction defaultAction;

    public MainRequestProcessor(IAction DefaultAction, Dao dao) {
        this.defaultAction = DefaultAction;
        this.dao = dao;
    }
    
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        String actName = request.getParameter("action");
        IAction action = actions.get(actName);
        
        if (action != null) {
            actions.get(actName).execute(request, response, dao);
        } else {
            defaultAction.execute(request, response, dao);
        }
    }

    @Override
    public void registerAction(IAction action) {
        actions.put(action.getName(), action);
    }
    
}
