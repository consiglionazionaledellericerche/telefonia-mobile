package si.cnr.it.domain;

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
    @Column(name = "data_attivazione", nullable = false)
    private LocalDate dataAttivazione;

    @Column(name = "data_cessazione")
    private LocalDate dataCessazione;

    @NotNull
    @Column(name = "intestatario_contratto", nullable = false)
    private String intestatarioContratto;

    @NotNull
    @Column(name = "numero_contratto", nullable = false)
    private String numeroContratto;

    @NotNull
    @Column(name = "utenza_telefono", nullable = false)
    private String utenzaTelefono;

    @NotNull
    @Column(name = "istituto_telefono", nullable = false)
    private String istitutoTelefono;



    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    /** Prova Valerio */
//    @NotNull
//    //@Column(name = "ist", nullable = false)
//    private String ist = "";
//
//    public String getIst() {
//        return ist;
//    }
//
//    public Telefono ist(String ist) {
//        this.ist = ist;
//        return this;
//    }
//
//    public void setIst(String ist) {
//        this.ist = ist;
//    }
    /** Fine Prova Valerio */

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

    public LocalDate getDataAttivazione() {
        return dataAttivazione;
    }

    public Telefono dataAttivazione(LocalDate dataAttivazione) {
        this.dataAttivazione = dataAttivazione;
        return this;
    }

    public void setDataAttivazione(LocalDate dataAttivazione) {
        this.dataAttivazione = dataAttivazione;
    }

    public LocalDate getDataCessazione() {
        return dataCessazione;
    }

    public Telefono dataCessazione(LocalDate dataCessazione) {
        this.dataCessazione = dataCessazione;
        return this;
    }

    public void setDataCessazione(LocalDate dataCessazione) {
        this.dataCessazione = dataCessazione;
    }

    public String getIntestatarioContratto() {
        return intestatarioContratto;
    }

    public Telefono intestatarioContratto(String intestatarioContratto) {
        this.intestatarioContratto = intestatarioContratto;
        return this;
    }

    public void setIntestatarioContratto(String intestatarioContratto) {
        this.intestatarioContratto = intestatarioContratto;
    }

    public String getNumeroContratto() {
        return numeroContratto;
    }

    public Telefono numeroContratto(String numeroContratto) {
        this.numeroContratto = numeroContratto;
        return this;
    }

    public void setNumeroContratto(String numeroContratto) {
        this.numeroContratto = numeroContratto;
    }

    public String getUtenzaTelefono() {
        return utenzaTelefono;
    }

    public Telefono utenzaTelefono(String utenzaTelefono) {
        this.utenzaTelefono = utenzaTelefono;
        return this;
    }

    public void setUtenzaTelefono(String utenzaTelefono) {
        this.utenzaTelefono = utenzaTelefono;
    }

    public String getIstitutoTelefono() {
        return istitutoTelefono;
    }

    public Telefono istitutoTelefono(String istitutoTelefono) {
        this.istitutoTelefono = istitutoTelefono;
        return this;
    }

    public void setIstitutoTelefono(String istitutoTelefono) {
        this.istitutoTelefono = istitutoTelefono;
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
            ", dataAttivazione='" + getDataAttivazione() + "'" +
            ", dataCessazione='" + getDataCessazione() + "'" +
            ", intestatarioContratto='" + getIntestatarioContratto() + "'" +
            ", numeroContratto='" + getNumeroContratto() + "'" +
            ", utenzaTelefono='" + getUtenzaTelefono() + "'" +
            ", istitutoTelefono='" + getIstitutoTelefono() + "'" +
//            ", ist='" + getIst() + "'" +
            "}";
    }
}
