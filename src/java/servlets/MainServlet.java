package servlets;

import dao.daos.Dao;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.requestprocessors.*;

public class MainServlet extends HttpServlet {

    private Dao dao;
    private IRequestProcessor requestProcessor;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext ctx = config.getServletContext();
        String url = ctx.getInitParameter("DB_URL");
        String user = ctx.getInitParameter("USER");
        String pwd = ctx.getInitParameter("PSW");
        dao = new Dao(url, user, pwd);
        requestProcessor = new MainRequestProcessor(new DefaultAction(), dao);
        requestProcessor.registerAction(new LoginAction());
        requestProcessor.registerAction(new LogoutAction());
        requestProcessor.registerAction(new SignupAction());
        requestProcessor.registerAction(new GetRipetizioniDisponibiliAction());
        requestProcessor.registerAction(new LoggedInAction());
        requestProcessor.registerAction(new AdminAuthActionDecorator(new GetCorsiAction()));
        requestProcessor.registerAction(new AdminAuthActionDecorator(new ChangeNameCorsoAction()));
        requestProcessor.registerAction(new AdminAuthActionDecorator(new NewCorsoAction()));
        requestProcessor.registerAction(new AdminAuthActionDecorator(new DeleteCorsoAction()));
        requestProcessor.registerAction(new AdminAuthActionDecorator(new GetDocentiAction()));
        requestProcessor.registerAction(new AdminAuthActionDecorator(new ChangeNameDocenteAction()));
        requestProcessor.registerAction(new AdminAuthActionDecorator(new NewDocenteAction()));
        requestProcessor.registerAction(new AdminAuthActionDecorator(new DeleteDocenteAction()));
        requestProcessor.registerAction(new EditAccountAction());
        requestProcessor.registerAction(new DeleteAccountAction());
        requestProcessor.registerAction(new AdminAuthActionDecorator(new GetPrenotazioniAction()));
        requestProcessor.registerAction(new GetPrenotazioniUtenteAction());
        requestProcessor.registerAction(new CancelPrenotazioniUtenteAction());
        requestProcessor.registerAction(new AdminAuthActionDecorator(new GetDocenzeAction()));
        requestProcessor.registerAction(new AdminAuthActionDecorator(new DeleteDocenzaAction()));
        requestProcessor.registerAction(new AdminAuthActionDecorator(new NewDocenzaAction()));
        requestProcessor.registerAction(new AdminAuthActionDecorator(new GetUtentiAction()));
        requestProcessor.registerAction(new NewPrenotazioneAction());
        requestProcessor.registerAction(new AdminAuthActionDecorator(new DeleteUtenteAction()));
        requestProcessor.registerAction(new AdminAuthActionDecorator(new EditPasswordUtenteAction()));
        requestProcessor.registerAction(new AdminAuthActionDecorator(new ChangeRoleAction()));
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        requestProcessor.processRequest(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
