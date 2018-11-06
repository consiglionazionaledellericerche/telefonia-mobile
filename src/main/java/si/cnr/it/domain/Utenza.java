package si.cnr.it.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Utenza.
 */
@Entity
@Table(name = "utenza")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Utenza implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "matricola", nullable = false)
    private String matricola;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user_utenza;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Istituto istituto_user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricola() {
        return matricola;
    }

    public Utenza matricola(String matricola) {
        this.matricola = matricola;
        return this;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public User getUser_utenza() {
        return user_utenza;
    }

    public Utenza user_utenza(User user) {
        this.user_utenza = user;
        return this;
    }

    public void setUser_utenza(User user) {
        this.user_utenza = user;
    }

    public Istituto getIstituto_user() {
        return istituto_user;
    }

    public Utenza istituto_user(Istituto istituto) {
        this.istituto_user = istituto;
        return this;
    }

    public void setIstituto_user(Istituto istituto) {
        this.istituto_user = istituto;
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
        Utenza utenza = (Utenza) o;
        if (utenza.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), utenza.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Utenza{" +
            "id=" + getId() +
            ", matricola='" + getMatricola() + "'" +
            "}";
    }
}
