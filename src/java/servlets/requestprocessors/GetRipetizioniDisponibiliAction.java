package servlets.requestprocessors;

import dao.daos.Dao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.utils.SrvUtils;

public class GetRipetizioniDisponibiliAction implements IAction {
    
    private final String name;

    public GetRipetizioniDisponibiliAction(String name) {
        this.name = name;
    }

    public GetRipetizioniDisponibiliAction() {
        this.name = "get_ripetizioni_disponibili";
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Dao dao) {
        SrvUtils.sendJsonObject(response, dao.getRipetizioneDAO().findAllByStatoDisponibile());
    }

    @Override
    public String getName() {
        return name;
    }
}
