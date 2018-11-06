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

    @NotNull
    @Column(name = "altro", nullable = false)
    private String altro;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Telefono telefono_telser;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Servizi servizi_telser;

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

    public Telefono getTelefono_telser() {
        return telefono_telser;
    }

    public Telefonoservizi telefono_telser(Telefono telefono) {
        this.telefono_telser = telefono;
        return this;
    }

    public void setTelefono_telser(Telefono telefono) {
        this.telefono_telser = telefono;
    }

    public Servizi getServizi_telser() {
        return servizi_telser;
    }

    public Telefonoservizi servizi_telser(Servizi servizi) {
        this.servizi_telser = servizi;
        return this;
    }

    public void setServizi_telser(Servizi servizi) {
        this.servizi_telser = servizi;
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
