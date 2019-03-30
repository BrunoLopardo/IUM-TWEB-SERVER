package dao.dto;

import java.util.Objects;

public class Docenza {

    private Corso corso;
    private Docente docente;

    public Docenza(Corso corso, Docente docente) {
        this.corso = corso;
        this.docente = docente;
    }

    public Docenza() {}

    public Corso getCorso() {
        return corso;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setCorso(Corso corso) {
        this.corso = corso;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    @Override
    public String toString() {
        return "Docenza{" + "corso=" + corso + ", docente=" + docente + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.corso);
        hash = 67 * hash + Objects.hashCode(this.docente);
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
        final Docenza other = (Docenza) obj;
        if (!Objects.equals(this.corso, other.corso)) {
            return false;
        }
        if (!Objects.equals(this.docente, other.docente)) {
            return false;
        }
        return true;
    }

}
