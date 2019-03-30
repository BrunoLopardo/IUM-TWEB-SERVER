package servlets.requestprocessors;

import dao.dto.Corso;
import dao.daos.Dao;
import dao.exceptions.DAOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class ChangeNameCorsoAction implements IAction {

    private final String name;

    public ChangeNameCorsoAction(String name) {
        this.name = name;
    }

    public ChangeNameCorsoAction() {
        this.name = "change_nome_corso";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        String nomeCorso = request.getParameter("nome");
        int id = 0;
        
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch(NumberFormatException e) {
            SrvUtils.sendStatusCode(response, 5); // Non è un numero oppure è null
            return;
        }

        if (nomeCorso == null || nomeCorso.isEmpty()) {
            SrvUtils.sendStatusCode(response, 1); // Il nome non può essere vuoto
            return;
        }
        
        try {
            Corso newCorso = new Corso(id, nomeCorso);
            dao.getCorsoDAO().update(newCorso);
            SrvUtils.sendStatusCode(response, 0); // Success
        } catch(DAOException e) {
            switch (e.getErrorCode()) {
                case DAOException.UNIQUE_VIOLATION:
                    SrvUtils.sendStatusCode(response, 2); // Esiste già un corso con quel nome
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
