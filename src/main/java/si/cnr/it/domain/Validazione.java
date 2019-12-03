package si.cnr.it.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Validazione.
 */
@Entity
@Table(name = "validazione")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Validazione implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "data_modifica", nullable = false)
    private LocalDate dataModifica;

    @NotNull
    @Column(name = "descrizione", nullable = false)
    private String descrizione;

    @Lob
    @Column(name = "documento_firmato")
    private byte[] documentoFirmato;

    @Column(name = "documento_firmato_content_type")
    private String documentoFirmatoContentType;

    @Column(name = "data_validazione")
    private ZonedDateTime dataValidazione;

    @Column(name = "user_direttore")
    private String userDirettore;

    @Column(name = "ip_validazione")
    private String ipValidazione;

    @Column(name = "id_flusso")
    private String idFlusso;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Telefono validazioneTelefono;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataModifica() {
        return dataModifica;
    }

    public Validazione dataModifica(LocalDate dataModifica) {
        this.dataModifica = dataModifica;
        return this;
    }

    public void setDataModifica(LocalDate dataModifica) {
        this.dataModifica = dataModifica;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Validazione descrizione(String descrizione) {
        this.descrizione = descrizione;
        return this;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public byte[] getDocumentoFirmato() {
        return documentoFirmato;
    }

    public Validazione documentoFirmato(byte[] documentoFirmato) {
        this.documentoFirmato = documentoFirmato;
        return this;
    }

    public void setDocumentoFirmato(byte[] documentoFirmato) {
        this.documentoFirmato = documentoFirmato;
    }

    public String getDocumentoFirmatoContentType() {
        return documentoFirmatoContentType;
    }

    public Validazione documentoFirmatoContentType(String documentoFirmatoContentType) {
        this.documentoFirmatoContentType = documentoFirmatoContentType;
        return this;
    }

    public void setDocumentoFirmatoContentType(String documentoFirmatoContentType) {
        this.documentoFirmatoContentType = documentoFirmatoContentType;
    }

    public ZonedDateTime getDataValidazione() {
        return dataValidazione;
    }

    public Validazione dataValidazione(ZonedDateTime dataValidazione) {
        this.dataValidazione = dataValidazione;
        return this;
    }

    public void setDataValidazione(ZonedDateTime dataValidazione) {
        this.dataValidazione = dataValidazione;
    }

    public String getUserDirettore() {
        return userDirettore;
    }

    public Validazione userDirettore(String userDirettore) {
        this.userDirettore = userDirettore;
        return this;
    }

    public void setUserDirettore(String userDirettore) {
        this.userDirettore = userDirettore;
    }

    public String getIpValidazione() {
        return ipValidazione;
    }

    public Validazione ipValidazione(String ipValidazione) {
        this.ipValidazione = ipValidazione;
        return this;
    }

    public void setIpValidazione(String ipValidazione) {
        this.ipValidazione = ipValidazione;
    }

    public String getIdFlusso() {
        return idFlusso;
    }

    public Validazione idFlusso(String idFlusso) {
        this.idFlusso = idFlusso;
        return this;
    }

    public void setIdFlusso(String idFlusso) {
        this.idFlusso = idFlusso;
    }

    public Telefono getValidazioneTelefono() {
        return validazioneTelefono;
    }

    public Validazione validazioneTelefono(Telefono telefono) {
        this.validazioneTelefono = telefono;
        return this;
    }

    public void setValidazioneTelefono(Telefono telefono) {
        this.validazioneTelefono = telefono;
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
        Validazione validazione = (Validazione) o;
        if (validazione.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), validazione.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Validazione{" +
            "id=" + getId() +
            ", dataModifica='" + getDataModifica() + "'" +
            ", descrizione='" + getDescrizione() + "'" +
            ", documentoFirmato='" + getDocumentoFirmato() + "'" +
            ", documentoFirmatoContentType='" + getDocumentoFirmatoContentType() + "'" +
            ", dataValidazione='" + getDataValidazione() + "'" +
            ", userDirettore='" + getUserDirettore() + "'" +
            ", ipValidazione='" + getIpValidazione() + "'" +
            ", idFlusso='" + getIdFlusso() + "'" +
            "}";
    }
}
