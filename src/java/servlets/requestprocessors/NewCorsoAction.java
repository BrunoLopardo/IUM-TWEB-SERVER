package servlets.requestprocessors;

import dao.dto.Corso;
import dao.daos.Dao;
import dao.exceptions.DAOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class NewCorsoAction implements IAction {

    private final String name;

    public NewCorsoAction(String name) {
        this.name = name;
    }

    public NewCorsoAction() {
        this.name = "new_corso";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        String titolo = request.getParameter("nome");

        if (titolo == null || titolo.isEmpty()) {
            SrvUtils.sendStatusCode(response, 1); // Il nome non può essere vuoto
            return;
        }

        try {
            Corso newCorso = new Corso(null, titolo);
            dao.getCorsoDAO().create(newCorso);
            SrvUtils.sendResponse(response, "{ \"statusCode\": 0, \"id\": " + newCorso.getID() + " }"); // Success
        } catch (DAOException e) {
            switch (e.getErrorCode()) {
                case DAOException.UNIQUE_VIOLATION:
                    SrvUtils.sendStatusCode(response, 2); // Nome corso già utilizzato
                    break;
                case DAOException.STRING_DATA_RIGHT_TRUNCATION:
                    SrvUtils.sendStatusCode(response, 4); // Nome corso troppo lungo
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
