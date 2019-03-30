package servlets.requestprocessors;

import dao.daos.Dao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class DeleteDocenteAction implements IAction {

    private final String name;

    public DeleteDocenteAction(String name) {
        this.name = name;
    }

    public DeleteDocenteAction() {
        this.name = "delete_docente";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        int id = 0;
        
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch(NumberFormatException e) {
            SrvUtils.sendStatusCode(response, 1); // Non è un numero oppure è null
            return;
        }

        if (dao.getDocenteDAO().findByID(id) != null) {
            dao.getDocenteDAO().deleteByID(id);
            SrvUtils.sendStatusCode(response, 0); // Success
        } else {
            SrvUtils.sendStatusCode(response, 2); // Corso inesistente
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
