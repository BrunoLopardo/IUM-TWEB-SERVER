package dao.dto;

import java.util.Objects;

public class Docente {

    private Integer ID;
    private String nome;
    private String cognome;

    public Docente(Integer ID, String nome, String cognome) {
        this.ID = ID;
        this.nome = nome;
        this.cognome = cognome;
    }

    public Docente() {}

    public Integer getID() {
        return ID;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    @Override
    public String toString() {
        return "Docente{ID=" + ID + ", nome=" + nome + ", cognome=" + cognome + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.ID);
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
        final Docente other = (Docente) obj;
        if (!Objects.equals(this.ID, other.ID)) {
            return false;
        }
        return true;
    }
}
