package dao.daos;

import dao.utils.ConnectionManager;

public class Dao {

    private final ConnectionManager connectionManager;
    private final UtenteDAO utenteDAO;
    private final CorsoDAO corsoDAO;
    private final DocenteDAO docenteDAO;
    private final DocenzaDAO docenzaDAO;
    private final PrenotazioneDAO prenotazioneDAO;
    private final RipetizioneDAO ripetizioneDAO;

    public Dao(String DB_URL, String USER, String PWD) {
        this.connectionManager = new ConnectionManager(DB_URL, USER, PWD);
        this.utenteDAO = new UtenteDAO(connectionManager);
        this.corsoDAO = new CorsoDAO(connectionManager);
        this.docenteDAO = new DocenteDAO(connectionManager);
        this.docenzaDAO = new DocenzaDAO(connectionManager);
        this.prenotazioneDAO = new PrenotazioneDAO(connectionManager);
        this.ripetizioneDAO = new RipetizioneDAO(connectionManager);
    }

    public UtenteDAO getUtenteDAO() {
        return utenteDAO;
    }

    public CorsoDAO getCorsoDAO() {
        return corsoDAO;
    }

    public DocenteDAO getDocenteDAO() {
        return docenteDAO;
    }

    public DocenzaDAO getDocenzaDAO() {
        return docenzaDAO;
    }

    public PrenotazioneDAO getPrenotazioneDAO() {
        return prenotazioneDAO;
    }

    public RipetizioneDAO getRipetizioneDAO() {
        return ripetizioneDAO;
    }
}
