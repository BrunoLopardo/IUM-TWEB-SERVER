package dao.utils;

/**
 * Contiene tutte le query usate nel DAO, Solo operazioni CRUD (Create, Read,
 * Update, Delete).
 *
 */
public class SQLQuery {

    /**
     * Inserisce un nuovo utente nel DB, Parametri: Username, Password, isAdmin.
     */
    public static final String CREATE_UTENTE = "INSERT INTO\n"
            + "Utente(Username, Password, isAdmin)\n"
            + "VALUES (?, ?, ?)";

    /**
     * Inserisce un nuovo docente nel DB, Parametri: Nome, Cognome.
     */
    public static final String CREATE_DOCENTE = "INSERT INTO\n"
            + "Docente(Nome, Cognome)\n"
            + "VALUES (?, ?)";

    /**
     * Inserisce un nuovo corso nel DB, Parametri: Titolo.
     */
    public static final String CREATE_CORSO = "INSERT INTO\n"
            + "Corso(Titolo)\n"
            + "VALUES (?)";

    /**
     * Inserisce una nuova docenza nel DB, Parametri: Corso.ID, Docente.ID.
     */
    public static final String CREATE_DOCENZA = "INSERT INTO\n"
            + "Docenza(Corso, Docente)\n"
            + "VALUES (?, ?)";

    /**
     * Inserisce una nuova prenotazione attiva nel DB, la data di creazione è
     * inserita in automatico, Parametri: Corso.ID, Docente.ID, Utente.ID,
     * Giorno, Ora_inizio.
     */
    public static final String CREATE_PRENOTAZIONE = "INSERT INTO\n"
            + "Prenotazione(Corso, Docente, Utente, Stato, Giorno, Ora_inizio, created_at)\n"
            + "VALUES (?, ?, ?, 'Attiva', ?, ?, (\n"
            + "    SELECT EXTRACT(EPOCH FROM (SELECT NOW() AT TIME ZONE 'UTC')\n"
            + ") * 1000))";

    /**
     * Ottiene le informazioni dell'utente con l'ID specificato, Parametri: ID.
     */
    public static final String FIND_BY_ID_UTENTE = "SELECT *\n"
            + "FROM Utente\n"
            + "WHERE ID = ?";
    
    /**
     * Ottiene le informazioni del corso con l'ID specificato, 
     * Parametri: ID.
     */
    public static final String FIND_BY_ID_CORSO = "SELECT *\n"
            + "FROM Corso\n"
            + "WHERE ID = ?";
    
        /**
     * Ottiene le informazioni del docente con l'ID specificato, 
     * Parametri: ID.
     */
    public static final String FIND_BY_ID_DOCENTE = "SELECT *\n"
            + "FROM Docente\n"
            + "WHERE ID = ?";
    
    /**
     * Ottiene le informazioni della docenza con gli ID specificati, 
     * Parametri: ID docente, ID corso.
     */
    public static final String FIND_BY_ID_DOCENZA = "SELECT Corso, Titolo, Docente, Nome, Cognome\n"
            + "FROM Docente JOIN Docenza ON Docente.ID = Docenza.Docente\n"
            + "JOIN Corso ON Corso.ID = Docenza.Corso\n"
            + "WHERE Docente = ? AND Corso = ?";
    
    /**
     * Ottiene le informazioni della prenotazione con l'ID specificato,
     * Parametri: ID.
     */
    public static final String FIND_BY_ID_PRENOTAZIONE = "SELECT Prenotazione.ID AS Prenotazione_ID, Corso.ID AS Corso_ID,\n"
            + "Titolo, Docente.ID AS Docente_ID, Utente.ID AS Utente_ID, Nome, Cognome, Stato, Giorno, Ora_inizio, Created_at,\n"
            + "Username, Password, isAdmin\n"
            + "FROM Prenotazione JOIN Corso ON Prenotazione.Corso = Corso.ID\n"
            + "JOIN Docente ON Prenotazione.Docente = Docente.ID\n"
            + "JOIN Utente ON Prenotazione.Utente = Utente.ID\n"
            + "WHERE Prenotazione.ID = ?";
    
    /**
     * Ottiene le informazioni dell'utente con lo username specificato,
     * Parametri: Username.
     */
    public static final String FIND_BY_USERNAME_UTENTE = "SELECT *\n"
            + "FROM Utente\n"
            + "WHERE Username = ?";

    /**
     * Ottiene le informazioni del docente con il nome e il cognome specificati,
     * Parametri: Nome, Cognome.
     */
    public static final String FIND_BY_NOME_AND_COGNOME_DOCENTE = "SELECT *\n"
            + "FROM Docente\n"
            + "WHERE Nome=? AND Cognome=?";
    
    /**
     * Ottiene le informazioni del corso con il titolo specificato, Parametri:
     * Titolo.
     */
    public static final String FIND_BY_TITOLO_CORSO = "SELECT *\n"
            + "FROM Corso\n"
            + "WHERE Titolo=?";

    /**
     * Ottiene tutte le docenze, coi dati relativi ai corsi e ai docenti.
     */
    public static final String FIND_ALL_DOCENZA = "SELECT Corso.ID Corso_ID, Docente.ID Docente_ID, *\n"
            + "FROM Docente JOIN Docenza ON Docente.ID = Docenza.Docente\n"
            + "JOIN Corso ON Corso.ID = Docenza.Corso";
    
    /**
     * Ottiene tutti le informazioni di tutti gli utenti.
     */
    public static final String FIND_ALL_UTENTE = "SELECT *\n"
            + "FROM Utente";
    
    /**
     * Ottiene tutti le informazioni di tutti i docenti.
     */
    public static final String FIND_ALL_DOCENTE = "SELECT *\n"
            + "FROM Docente";
    
    /**
     * Ottiene tutti le informazioni di tutti i docenti.
     */
    public static final String FIND_ALL_CORSO = "SELECT *\n"
            + "FROM Corso";
    
    /**
     * Ottiene le informazioni di tutte le prenotazioni esistenti.
     */
    public static final String FIND_ALL_PRENOTAZIONE = "SELECT Prenotazione.ID AS Prenotazione_ID, Corso.ID AS Corso_ID,\n"
            + "Titolo, Docente.ID AS Docente_ID, Utente.ID AS Utente_ID, Nome, Cognome, Stato, Giorno, Ora_inizio, Created_at,\n"
            + "Username, Password, isAdmin\n"
            + "FROM Prenotazione JOIN Corso ON Prenotazione.Corso = Corso.ID\n"
            + "JOIN Docente ON Prenotazione.Docente = Docente.ID\n"
            + "JOIN Utente ON Prenotazione.Utente = Utente.ID";
    
    /**
     * Ottiene le informazioni di tutte le ripetizioni esistenti.
     */
    public static final String FIND_ALL_RIPETIZIONE = "SELECT\n"
            + "Docente.ID Docente_ID, Corso.ID Corso_ID, *\n"
            + "FROM Docente JOIN Docenza ON Docente.ID = Docenza.Docente\n"
            + "JOIN Corso ON Corso.ID = Docenza.Corso\n"
            + "CROSS JOIN (VALUES (15), (16), (17), (18)) t(Ora_inizio)\n"
            + "CROSS JOIN (VALUES (CAST('Lun' AS day_name)), \n"
            + "                    (CAST('Mar' AS day_name)),\n"
            + "                    (CAST('Mer' AS day_name)),\n"
            + "                    (CAST('Gio' AS day_name)),\n"
            + "                    (CAST('Ven' AS day_name))) p(Giorno)";
    
    /**
     * Ottiene le informazioni di tutti i corsi attivi.
     */
    public static final String FIND_ALL_ACTIVE_CORSO = "SELECT DISTINCT Corso.*\n"
            + "FROM Corso JOIN Docenza on Corso.ID = Docenza.Corso";
    
    /**
     * Ottiene le informazioni di tutti i docenti attivi.
     */
    public static final String FIND_ALL_ACTIVE_DOCENTE = "SELECT DISTINCT Docente.*\n"
            + "FROM Docente JOIN Docenza on Docente.ID = Docenza.Docente";
    
    /**
     * Ottiene tutte le prenotazioni di un utente specificato, Parametri:
     * Utente.ID.
     */
    public static final String FIND_ALL_BY_UTENTE_PRENOTAZIONE = "SELECT\n"
            + "Corso.ID Corso_ID, Docente.ID Docente_ID, Utente.ID Utente_ID, Prenotazione.ID Prenotazione_ID, *\n"
            + "FROM Prenotazione JOIN Docente ON Prenotazione.Docente = Docente.ID\n"
            + "JOIN Corso ON Prenotazione.Corso = Corso.ID\n"
            + "JOIN Utente ON Prenotazione.Utente = Utente.ID\n"
            + "WHERE Utente = ?";

    /**
     * Trova tutte le ripetizioni disponibili.
     */
    public static final String FIND_ALL_BY_STATO_DISPONIBILE_RIPETIZIONE = "SELECT\n"
            + "Docente.ID Docente_ID, Corso.ID Corso_ID, *\n"
            + "FROM Docente JOIN Docenza ON Docente.ID = Docenza.Docente\n"
            + "JOIN Corso ON Corso.ID = Docenza.Corso\n"
            + "CROSS JOIN (VALUES (15), (16), (17), (18)) t(Ora_inizio)\n"
            + "CROSS JOIN (VALUES (CAST('Lun' AS day_name)), \n"
            + "                    (CAST('Mar' AS day_name)),\n"
            + "                    (CAST('Mer' AS day_name)),\n"
            + "                    (CAST('Gio' AS day_name)),\n"
            + "                    (CAST('Ven' AS day_name))) p(Giorno)\n"
            + "WHERE (Docente.ID, Giorno, Ora_inizio) NOT IN\n"
            + "(\n"
            + "	SELECT Docente, Giorno, Ora_inizio\n"
            + "	FROM Prenotazione\n"
            + "	WHERE Stato = 'Attiva'\n"
            + ");";
    
    /**
     * Ottiene le informazioni delle docenze del docente specificato,
     * Parametri: ID docente.
     */
    public static final String FIND_ALL_BY_DOCENTE_DOCENZA = "SELECT Corso, Titolo, Docente, Nome, Cognome\n"
            + "FROM Docente JOIN Docenza ON Docente.ID = Docenza.Docente\n"
            + "JOIN Corso ON Corso.ID = Docenza.Corso\n"
            + "WHERE Docente = ?";
    
    /**
     * Ottiene le informazioni delle docenze di un corso specificato,
     * Parametri: ID corso.
     */
    public static final String FIND_ALL_BY_CORSO_DOCENZA = "SELECT Corso, Titolo, Docente, Nome, Cognome\n"
            + "FROM Docente JOIN Docenza ON Docente.ID = Docenza.Docente\n"
            + "JOIN Corso ON Corso.ID = Docenza.Corso\n"
            + "WHERE Corso = ?";
    
    /**
     * Ottiene le informazioni della prenotazione con l'ID del corso specificato,
     * Parametri: ID corso.
     */
    public static final String FIND_ALL_BY_CORSO_PRENOTAZIONE = "SELECT Prenotazione.ID AS Prenotazione_ID, Corso.ID AS Corso_ID,\n"
            + "Titolo, Docente.ID AS Docente_ID, Utente.ID AS Utente_ID, Nome, Cognome, Stato, Giorno, Ora_inizio, Created_at,\n"
            + "Username, Password, isAdmin\n"
            + "FROM Prenotazione JOIN Corso ON Prenotazione.Corso = Corso.ID\n"
            + "JOIN Docente ON Prenotazione.Docente = Docente.ID\n"
            + "JOIN Utente ON Prenotazione.Utente = Utente.ID\n"
            + "WHERE Corso.ID = ?";
    
    /**
     * Ottiene le informazioni della prenotazione con l'ID del docente
     * specificato, Parametri: ID docente.
     */
    public static final String FIND_ALL_BY_DOCENTE_PRENOTAZIONE = "SELECT Prenotazione.ID AS Prenotazione_ID, Corso.ID AS Corso_ID,\n"
            + "Titolo, Docente.ID AS Docente_ID, Utente.ID AS Utente_ID, Nome, Cognome, Stato, Giorno, Ora_inizio, Created_at,\n"
            + "Username, Password, isAdmin\n"
            + "FROM Prenotazione JOIN Corso ON Prenotazione.Corso = Corso.ID\n"
            + "JOIN Docente ON Prenotazione.Docente = Docente.ID\n"
            + "JOIN Utente ON Prenotazione.Utente = Utente.ID\n"
            + "WHERE Docente.ID = ?\n";
    
    /**
     * Ottiene le informazioni della prenotazione con lo stato specificato,
     * Parametri: stato.
     */
    public static final String FIND_ALL_BY_STATO_PRENOTAZIONE = "SELECT Prenotazione.ID AS Prenotazione_ID, Corso.ID AS Corso_ID,\n"
            + "Titolo, Docente.ID AS Docente_ID, Utente.ID AS Utente_ID, Nome, Cognome, Stato, Giorno, Ora_inizio, Created_at,\n"
            + "Username, Password, isAdmin\n"
            + "FROM Prenotazione JOIN Corso ON Prenotazione.Corso = Corso.ID\n"
            + "JOIN Docente ON Prenotazione.Docente = Docente.ID\n"
            + "JOIN Utente ON Prenotazione.Utente = Utente.ID\n"
            + "WHERE Prenotazione.Stato = ?";
    
    /**
     * Trova tutte le ripetizioni disponibili di uno specificato docente,
     * Parametri: ID docente.
     */
    public static final String FIND_ALL_BY_DOCENTE_AND_STATO_DISPONIBILE_RIPETIZIONE = "SELECT\n"
            + "Docente.ID Docente_ID, Corso.ID Corso_ID, *\n"
            + "FROM Docente JOIN Docenza ON Docente.ID = Docenza.Docente\n"
            + "JOIN Corso ON Corso.ID = Docenza.Corso\n"
            + "CROSS JOIN (VALUES (15), (16), (17), (18)) t(Ora_inizio)\n"
            + "CROSS JOIN (VALUES (CAST('Lun' AS day_name)), \n"
            + "                    (CAST('Mar' AS day_name)),\n"
            + "                    (CAST('Mer' AS day_name)),\n"
            + "                    (CAST('Gio' AS day_name)),\n"
            + "                    (CAST('Ven' AS day_name))) p(Giorno)\n"
            + "WHERE (Docente.ID, Giorno, Ora_inizio) NOT IN\n"
            + "(\n"
            + "	SELECT Docente, Giorno, Ora_inizio\n"
            + "	FROM Prenotazione\n"
            + "	WHERE Stato = 'Attiva'\n"
            + ") AND Docente.ID = ?";
    
    /**
     * Trova tutte le ripetizioni disponibili di uno specificato corso,
     * Parametri: ID corso.
     */
    public static final String FIND_ALL_BY_CORSO_AND_STATO_DISPONIBILE_RIPETIZIONE = "SELECT\n"
            + "Docente.ID Docente_ID, Corso.ID Corso_ID, *\n"
            + "FROM Docente JOIN Docenza ON Docente.ID = Docenza.Docente\n"
            + "JOIN Corso ON Corso.ID = Docenza.Corso\n"
            + "CROSS JOIN (VALUES (15), (16), (17), (18)) t(Ora_inizio)\n"
            + "CROSS JOIN (VALUES (CAST('Lun' AS day_name)), \n"
            + "                    (CAST('Mar' AS day_name)),\n"
            + "                    (CAST('Mer' AS day_name)),\n"
            + "                    (CAST('Gio' AS day_name)),\n"
            + "                    (CAST('Ven' AS day_name))) p(Giorno)\n"
            + "WHERE (Docente.ID, Giorno, Ora_inizio) NOT IN\n"
            + "(\n"
            + "	SELECT Docente, Giorno, Ora_inizio\n"
            + "	FROM Prenotazione\n"
            + "	WHERE Stato = 'Attiva'\n"
            + ") AND Corso.ID = ?";
    
    /**
     * Trova tutte le ripetizioni disponibili di uno specificato corso e docente,
     * Parametri: ID corso, ID docente.
     */
    public static final String FIND_ALL_BY_DOCENTE_AND_CORSO_AND_STATO_DISPONIBILE_RIPETIZIONE = "SELECT\n"
            + "Docente.ID Docente_ID, Corso.ID Corso_ID, *\n"
            + "FROM Docente JOIN Docenza ON Docente.ID = Docenza.Docente\n"
            + "JOIN Corso ON Corso.ID = Docenza.Corso\n"
            + "CROSS JOIN (VALUES (15), (16), (17), (18)) t(Ora_inizio)\n"
            + "CROSS JOIN (VALUES (CAST('Lun' AS day_name)), \n"
            + "                    (CAST('Mar' AS day_name)),\n"
            + "                    (CAST('Mer' AS day_name)),\n"
            + "                    (CAST('Gio' AS day_name)),\n"
            + "                    (CAST('Ven' AS day_name))) p(Giorno)\n"
            + "WHERE (Docente.ID, Giorno, Ora_inizio) NOT IN\n"
            + "(\n"
            + "	SELECT Docente, Giorno, Ora_inizio\n"
            + "	FROM Prenotazione\n"
            + "	WHERE Stato = 'Attiva'\n"
            + ") AND Corso.ID = ? AND Docente.ID = ?";
    
    /**
     * Aggiorna le informazioni dell'utente con l'ID specificato, Parametri:
     * Username, Password, isAdmin, ID.
     */
    public static final String UPDATE_UTENTE = "UPDATE Utente\n"
            + "SET Username=?, Password=?, isAdmin=?\n"
            + "WHERE ID = ?";
    
    /**
     * Aggiorna le informazioni del docente con l'ID specificato, Parametri:
     * Nome, Cognome, ID.
     */
    public static final String UPDATE_DOCENTE = "UPDATE Docente\n"
            + "SET Nome=?, Cognome=?\n"
            + "WHERE ID = ?";

    /**
     * Aggiorna le informazioni del corso con l'ID specificato, Parametri:
     * Titolo, ID.
     */
    public static final String UPDATE_CORSO = "UPDATE Corso\n"
            + "SET Titolo=?\n"
            + "WHERE ID = ?";
    
    /**
     * Aggiorna lo stato della prenotazione con lo stato specificato, 
     * Parametri: Stato, ID.
     */
    public static final String UPDATE_STATO_PRENOTAZIONE = "UPDATE Prenotazione\n"
            + "SET Stato=?\n"
            + "WHERE ID = ?";
    
    /**
     * Cancella il corso SENZA rimuoverlo dal DB, ovvero rimuove tutte le
     * docenze di un corso specificato,
     * Parametri: ID corso.
     */
    public static final String DELETE_BY_CORSO_DOCENZA = "DELETE FROM Docenza\n"
            + "WHERE Corso = ?";
    
    /**
     * Cancella il docente SENZA rimuoverlo dal DB, ovvero rimuove tutte le
     * docenze del docente specificato,
     * Parametri: ID docente.
     */
    public static final String DELETE_BY_DOCENTE_DOCENZA = "DELETE FROM Docenza\n"
            + "WHERE Docente = ?";
    
    /**
     * Cancella una docenza dal database,
     * Parametri: ID corso, ID docente.
     */
    public static final String DELETE_BY_ID_DOCENZA = "DELETE FROM Docenza\n"
            + "WHERE Corso = ? AND Docente = ?";
    
    /**
     * Cancella l'utente dal database assieme ai dati ad esso correlati,
     * Parametri: ID utente.
     */
    public static final String DELETE_BY_ID_UTENTE = "DELETE FROM Utente\n"
            + "WHERE ID = ?";
    
    /**
     * Cancella l'utente dal database assieme ai dati ad esso correlati,
     * Parametri: Username utente.
     */
    public static final String DELETE_BY_USERNAME_UTENTE = "DELETE FROM Utente\n"
            + "WHERE Username = ?";
    
     /**
     * Controlla se una determinata ripetizione esiste.
     * Parametri: ID corso, ID docente, giorno e ora.
     */
    public static final String RIPETIZIONE_EXISTS = "SELECT\n"
            + "Docente.ID Docente_ID, Corso.ID Corso_ID, *\n"
            + "FROM Docente JOIN Docenza ON Docente.ID = Docenza.Docente\n"
            + "JOIN Corso ON Corso.ID = Docenza.Corso\n"
            + "CROSS JOIN (VALUES (15), (16), (17), (18)) t(Ora_inizio)\n"
            + "CROSS JOIN (VALUES (CAST('Lun' AS day_name)), \n"
            + "                    (CAST('Mar' AS day_name)),\n"
            + "                    (CAST('Mer' AS day_name)),\n"
            + "                    (CAST('Gio' AS day_name)),\n"
            + "                    (CAST('Ven' AS day_name))) p(Giorno)\n"
            + "WHERE (Docente.ID, Giorno, Ora_inizio) NOT IN\n"
            + "(\n"
            + "	SELECT Docente, Giorno, Ora_inizio\n"
            + "	FROM Prenotazione\n"
            + "	WHERE Stato = 'Attiva'\n"
            + ") AND Corso.ID = ? AND Docente.ID = ? AND Giorno = ? AND Ora_inizio = ?";
    
    /**
     * Aggiorna le prenotazioni attive il cui corso è stato cancellato.
     * Parametri: ID corso.
     */
    public static final String UPDATE_PRENOTAZIONI_ATTIVE_BY_CORSO = "UPDATE Prenotazione\n"
            + "SET stato = 'Disdetta'\n"
            + "WHERE Corso = ? AND Stato = 'Attiva'";
    
     /**
     * Aggiorna le prenotazioni attive il cui docente è stato cancellato.
     * Parametri: ID docente.
     */
    public static final String UPDATE_PRENOTAZIONI_ATTIVE_BY_DOCENTE = "UPDATE Prenotazione\n"
            + "SET stato = 'Disdetta'\n"
            + "WHERE Docente = ? AND Stato = 'Attiva'";
    
    /**
     * Aggiorna le prenotazioni attive la cui docenza è stata cancellata.
     * Parametri: ID docente, ID corso.
     */
    public static final String UPDATE_PRENOTAZIONI_ATTIVE_BY_DOCENZA = "UPDATE Prenotazione\n"
            + "SET stato = 'Disdetta'\n"
            + "WHERE Docente = ? AND Corso = ? AND Stato = 'Attiva'";
    
    /**
     * Aggiorna lo stato delle prenotazioni da 'Attiva' a 'Effettuata' in base
     * alla data e l'ora corrente.
     */
    public static final String AUTO_UPDATE_STATO_PRENOTAZIONE = "UPDATE Prenotazione p1\n"
            + "SET Stato = 'Effettuata'\n"
            + "WHERE\n"
            + "Stato = 'Attiva' AND\n"
            + "(SELECT EXTRACT(EPOCH FROM (SELECT NOW() AT TIME ZONE 'UTC')))\n"
            + "> (\n"
            + "	SELECT CASE\n"
            + "		WHEN created_at < 1000 * ((EXTRACT(EPOCH FROM date_trunc('week', to_timestamp(created_at / 1000)))) + 3600 * 24 * id_giorno + 3600 * ora_inizio)\n"
            + "		THEN (EXTRACT(EPOCH FROM date_trunc('week', to_timestamp(created_at / 1000)))) + 3600 * 24 * id_giorno + 3600 * ora_inizio\n"
            + "		ELSE (EXTRACT(EPOCH FROM date_trunc('week', to_timestamp(created_at / 1000)))) + 3600 * 24 * id_giorno + 3600 * ora_inizio + 3600 * 24 * 7\n"
            + "	END AS rip_timestamp\n"
            + "	FROM prenotazione p2\n"
            + "	JOIN\n"
            + "	(\n"
            + "	VALUES (CAST('Lun' AS day_name), 0),\n"
            + "		   (CAST('Mar' AS day_name), 1),\n"
            + "		   (CAST('Mer' AS day_name), 2),\n"
            + "		   (CAST('Gio' AS day_name), 3),\n"
            + "		   (CAST('Ven' AS day_name), 4)\n"
            + "	) s(nome_giorno, id_giorno) ON p2.giorno = nome_giorno\n"
            + "	WHERE p2.id = p1.id\n"
            + ");";
}
