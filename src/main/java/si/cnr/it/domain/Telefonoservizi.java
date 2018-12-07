package si.cnr.it.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Telefonoservizi.
 */
@Entity
@Table(name = "telefonoservizi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Telefonoservizi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "altro")
    private String altro;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Telefono telefono;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Servizi servizi;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAltro() {
        return altro;
    }

    public Telefonoservizi altro(String altro) {
        this.altro = altro;
        return this;
    }

    public void setAltro(String altro) {
        this.altro = altro;
    }

    public Telefono getTelefono() {
        return telefono;
    }

    public Telefonoservizi telefono(Telefono telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(Telefono telefono) {
        this.telefono = telefono;
    }

    public Servizi getServizi() {
        return servizi;
    }

    public Telefonoservizi servizi(Servizi servizi) {
        this.servizi = servizi;
        return this;
    }

    public void setServizi(Servizi servizi) {
        this.servizi = servizi;
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
        Telefonoservizi telefonoservizi = (Telefonoservizi) o;
        if (telefonoservizi.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), telefonoservizi.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Telefonoservizi{" +
            "id=" + getId() +
            ", altro='" + getAltro() + "'" +
            "}";
    }
}
