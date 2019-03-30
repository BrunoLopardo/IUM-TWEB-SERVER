package dao.dto;

import java.util.Objects;

public class Corso {

    private Integer ID;
    private String titolo;

    public Corso(Integer ID, String titolo) {
        this.ID = ID;
        this.titolo = titolo;
    }
    
    public Corso() {}

    public Integer getID() {
        return ID;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    @Override
    public String toString() {
        return "Corso{ID=" + ID + ", titolo=" + titolo + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.ID);
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
        final Corso other = (Corso) obj;
        if (!Objects.equals(this.ID, other.ID)) {
            return false;
        }
        return true;
    }
}
