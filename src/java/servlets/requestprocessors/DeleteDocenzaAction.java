package servlets.requestprocessors;

import dao.daos.Dao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class DeleteDocenzaAction implements IAction {

    private final String name;

    public DeleteDocenzaAction(String name) {
        this.name = name;
    }

    public DeleteDocenzaAction() {
        this.name = "delete_docenza";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        
        int idCorso = 0;
        int idDocente = 0;
        
        try {
            idCorso = Integer.parseInt(request.getParameter("idCorso"));
            idDocente = Integer.parseInt(request.getParameter("idDocente"));
        } catch (NumberFormatException e) {
            SrvUtils.sendStatusCode(response, 1); // Non è un numero oppure è null
            return;
        }

        if (dao.getDocenzaDAO().findByID(idDocente, idCorso) != null) {
            dao.getDocenzaDAO().deleteByID(idDocente, idCorso);
            SrvUtils.sendStatusCode(response, 0); // Success
        } else {
            SrvUtils.sendStatusCode(response, 2); // Docenza inesistente
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
