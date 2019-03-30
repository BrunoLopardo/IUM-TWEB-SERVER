package dao.exceptions;

import java.sql.SQLException;
import org.postgresql.util.PSQLException;

public class DAOException extends RuntimeException {
    private final SQLException sqlException;

    // ------------------------ List of error codes -------------------------   
    /**
     * Errore lanciato quando si viola un vincolo unique.
     */
    public static final String UNIQUE_VIOLATION = "23505";
    
    /**
     * Errore lanciato quando si supera il limite di carattari di un varchar.
     */
    public static final String STRING_DATA_RIGHT_TRUNCATION = "22001";
    
    // ----------------------------------------------------------------------
    
    // ------------------------ List of constraints -------------------------
    
    public static final String PRENOTAZIONE_UTENTE_GIORNO_ORA_KEY = "prenotazione_utente_giorno_ora_key";
    
    public static final String PRENOTAZIONE_DOCENTE_GIORNO_ORA_KEY = "prenotazione_docente_giorno_ora_key";

    // ----------------------------------------------------------------------
    
    public DAOException(SQLException sqlException) {
        this.sqlException = sqlException;
    }
    
    public String getErrorCode() {
        // Postgres usa getSQLState() per gli errori
        return sqlException.getSQLState();
    }
    
    public String getViolatedConstraint() {
        PSQLException ex = (PSQLException) sqlException;
        return ex.getServerErrorMessage().getConstraint();
    }
    
    @Override
    public String getMessage() {
        return sqlException.getMessage();
    }
}
