package servlets.requestprocessors;

import dao.dto.Docente;
import dao.daos.Dao;
import dao.exceptions.DAOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class NewDocenteAction implements IAction {

    private final String name;

    public NewDocenteAction(String name) {
        this.name = name;
    }

    public NewDocenteAction() {
        this.name = "new_docente";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");

        if (nome == null || nome.isEmpty() || cognome == null || cognome.isEmpty()) {
            SrvUtils.sendStatusCode(response, 1); // Il nome e il cognome non possono essere vuoti
            return;
        }

        try {
            Docente newDocente = new Docente(null, nome, cognome);
            dao.getDocenteDAO().create(newDocente);
            SrvUtils.sendResponse(response, "{ \"statusCode\": 0, \"id\": " + newDocente.getID() + " }"); // Success
        } catch(DAOException e) {
            switch(e.getErrorCode()) {
                case DAOException.UNIQUE_VIOLATION:
                    SrvUtils.sendStatusCode(response, 2); // Nome docente già utilizzato
                    break;
                case DAOException.STRING_DATA_RIGHT_TRUNCATION:
                    SrvUtils.sendStatusCode(response, 4); // Il nome e/o il cognome del docente è troppo lungo
                    break;
                default:
                    SrvUtils.sendStatusCode(response, 99); // Unknown error
                    System.err.println(e.getMessage());
                    break;
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
