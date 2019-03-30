package dao.dto;

import java.util.Objects;

public class Utente {
    
    private Integer ID;
    private String username;
    private String password;
    private Boolean isAdmin;

    public Utente(Integer ID, String username, String password, Boolean isAdmin) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }
    
    public Utente() {}

    public Integer getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean isIsAdmin() {
        return isAdmin;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "Utente{ID=" + ID + ", username=" + username + ", password=" + password + ", isAdmin=" + isAdmin + "}";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.ID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Utente other = (Utente) obj;
        if (!Objects.equals(this.ID, other.ID)) {
            return false;
        }
        return true;
    }
}
