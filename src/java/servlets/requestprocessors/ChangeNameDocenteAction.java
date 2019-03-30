package servlets.requestprocessors;

import dao.dto.Docente;
import dao.daos.Dao;
import dao.exceptions.DAOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class ChangeNameDocenteAction implements IAction {

    private final String name;

    public ChangeNameDocenteAction(String name) {
        this.name = name;
    }

    public ChangeNameDocenteAction() {
        this.name = "change_nome_docente";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        String nomeDocente = request.getParameter("nome");
        String cognomeDocente = request.getParameter("cognome");
        int id = 0;
        
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch(NumberFormatException e) {
            SrvUtils.sendStatusCode(response, 5); // Non è un numero oppure è null
            return;
        }

        if (nomeDocente == null || nomeDocente.isEmpty() || cognomeDocente == null || cognomeDocente.isEmpty()) {
            SrvUtils.sendStatusCode(response, 1); // Il nome e il cognome non possono essere vuoti
            return;
        }
        
        try {
            Docente newDocente = new Docente(id, nomeDocente, cognomeDocente);
            dao.getDocenteDAO().update(newDocente);
            SrvUtils.sendStatusCode(response, 0); // Success
        } catch(DAOException e) {
            switch (e.getErrorCode()) {
                case DAOException.UNIQUE_VIOLATION:
                    SrvUtils.sendStatusCode(response, 2); // Esiste già un docente con quel nome
                    break;
                case DAOException.STRING_DATA_RIGHT_TRUNCATION:
                    SrvUtils.sendStatusCode(response, 4); // Nome e/o cognome docente troppo lungo
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
