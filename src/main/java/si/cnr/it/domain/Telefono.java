package si.cnr.it.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Telefono.
 */
@Entity
@Table(name = "telefono")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Telefono implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    private String numero;

    @NotNull
    @Column(name = "dataattivazione", nullable = false)
    private LocalDate dataattivazione;

    @Column(name = "datacessazione")
    private LocalDate datacessazione;

    @NotNull
    @Column(name = "intestatariocontratto", nullable = false)
    private String intestatariocontratto;

    @NotNull
    @Column(name = "numerocontratto", nullable = false)
    private String numerocontratto;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Utenza utenza_telefono;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Istituto istituto_telefono;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public Telefono numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDataattivazione() {
        return dataattivazione;
    }

    public Telefono dataattivazione(LocalDate dataattivazione) {
        this.dataattivazione = dataattivazione;
        return this;
    }

    public void setDataattivazione(LocalDate dataattivazione) {
        this.dataattivazione = dataattivazione;
    }

    public LocalDate getDatacessazione() {
        return datacessazione;
    }

    public Telefono datacessazione(LocalDate datacessazione) {
        this.datacessazione = datacessazione;
        return this;
    }

    public void setDatacessazione(LocalDate datacessazione) {
        this.datacessazione = datacessazione;
    }

    public String getIntestatariocontratto() {
        return intestatariocontratto;
    }

    public Telefono intestatariocontratto(String intestatariocontratto) {
        this.intestatariocontratto = intestatariocontratto;
        return this;
    }

    public void setIntestatariocontratto(String intestatariocontratto) {
        this.intestatariocontratto = intestatariocontratto;
    }

    public String getNumerocontratto() {
        return numerocontratto;
    }

    public Telefono numerocontratto(String numerocontratto) {
        this.numerocontratto = numerocontratto;
        return this;
    }

    public void setNumerocontratto(String numerocontratto) {
        this.numerocontratto = numerocontratto;
    }

    public Utenza getUtenza_telefono() {
        return utenza_telefono;
    }

    public Telefono utenza_telefono(Utenza utenza) {
        this.utenza_telefono = utenza;
        return this;
    }

    public void setUtenza_telefono(Utenza utenza) {
        this.utenza_telefono = utenza;
    }

    public Istituto getIstituto_telefono() {
        return istituto_telefono;
    }

    public Telefono istituto_telefono(Istituto istituto) {
        this.istituto_telefono = istituto;
        return this;
    }

    public void setIstituto_telefono(Istituto istituto) {
        this.istituto_telefono = istituto;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Telefono telefono = (Telefono) o;
        if (telefono.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), telefono.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Telefono{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", dataattivazione='" + getDataattivazione() + "'" +
            ", datacessazione='" + getDatacessazione() + "'" +
            ", intestatariocontratto='" + getIntestatariocontratto() + "'" +
            ", numerocontratto='" + getNumerocontratto() + "'" +
            "}";
    }
}
