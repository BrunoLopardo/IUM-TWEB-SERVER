package servlets.requestprocessors;

import dao.dto.Docenza;
import dao.dto.Corso;
import dao.dto.Docente;
import dao.daos.Dao;
import dao.exceptions.DAOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class NewDocenzaAction implements IAction {

    private final String name;

    public NewDocenzaAction(String name) {
        this.name = name;
    }

    public NewDocenzaAction() {
        this.name = "new_docenza";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        
        int idCorso = 0;
        int idDocente = 0;
        
        try {
            idCorso = Integer.parseInt(request.getParameter("idCorso"));
            idDocente = Integer.parseInt(request.getParameter("idDocente"));
        } catch (NumberFormatException e) {
            SrvUtils.sendStatusCode(response, 4); // Non è un numero oppure è null
            return;
        }
        
        if (idCorso == 0 || idDocente == 0) {
            SrvUtils.sendStatusCode(response, 1); // nessun docente e/o corso selezionati.
            return;
        }
        
        Docente docente = dao.getDocenteDAO().findByID(idDocente);
        Corso corso = dao.getCorsoDAO().findByID(idCorso);
        Docenza newDocenza = new Docenza(corso, docente);    
        
        try {
            dao.getDocenzaDAO().create(newDocenza);
            SrvUtils.sendStatusCode(response, 0); // Success       
        } catch (DAOException e) {
            if (e.getErrorCode().equals(DAOException.UNIQUE_VIOLATION)) {
                SrvUtils.sendStatusCode(response, 2); // Docenza già presente
            } else {
                SrvUtils.sendStatusCode(response, 99); // Si è verificato un errore
                System.err.println(e.getMessage());
            }
        } catch (NullPointerException e) {
            SrvUtils.sendStatusCode(response, 5); // idCorso e/o idDocente non presenti nel DB
        }
    }

    @Override
    public String getName() {
        return name;
    }

}
