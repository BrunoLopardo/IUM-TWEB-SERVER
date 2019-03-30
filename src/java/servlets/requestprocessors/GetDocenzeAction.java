package servlets.requestprocessors;

import dao.daos.Dao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class GetDocenzeAction implements IAction {
    
    private final String name;

    public GetDocenzeAction(String name) {
        this.name = name;
    }

    public GetDocenzeAction() {
        this.name = "get_docenze";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        SrvUtils.sendJsonObject(response, dao.getDocenzaDAO().findAll());
    }

    @Override
    public String getName() {
        return name;
    }
}
