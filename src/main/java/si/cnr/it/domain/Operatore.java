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
 * A Operatore.
 */
@Entity
@Table(name = "operatore")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Operatore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "data")
    private LocalDate data;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Telefono telefonoOperatore;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private ListaOperatori listaOperatori;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public Operatore data(LocalDate data) {
        this.data = data;
        return this;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Telefono getTelefonoOperatore() {
        return telefonoOperatore;
    }

    public Operatore telefonoOperatore(Telefono telefono) {
        this.telefonoOperatore = telefono;
        return this;
    }

    public void setTelefonoOperatore(Telefono telefono) {
        this.telefonoOperatore = telefono;
    }

    public ListaOperatori getListaOperatori() {
        return listaOperatori;
    }

    public Operatore listaOperatori(ListaOperatori listaOperatori) {
        this.listaOperatori = listaOperatori;
        return this;
    }

    public void setListaOperatori(ListaOperatori listaOperatori) {
        this.listaOperatori = listaOperatori;
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
        Operatore operatore = (Operatore) o;
        if (operatore.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), operatore.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Operatore{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            "}";
    }
}
