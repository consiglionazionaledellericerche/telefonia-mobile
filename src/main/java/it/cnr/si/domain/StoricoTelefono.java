/*
 * Copyright (C) 2020 Consiglio Nazionale delle Ricerche
 *
 *                 This program is free software: you can redistribute it and/or modify
 *                 it under the terms of the GNU Affero General Public License as
 *                 published by the Free Software Foundation, either version 3 of the
 *                 License, or (at your option) any later version.
 *
 *                 This program is distributed in the hope that it will be useful,
 *                 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *                 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *                 GNU Affero General Public License for more details.
 *
 *                 You should have received a copy of the GNU Affero General Public License
 *                 along with this program. If not, see https://www.gnu.org/licenses/
 */

package it.cnr.si.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A StoricoTelefono.
 */
@Entity
@Table(name = "storico_telefono")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StoricoTelefono implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "data_modifica", nullable = false)
    private Instant dataModifica;

    @NotNull
    @Column(name = "data_attivazione", nullable = false)
    private Instant dataAttivazione;

    @Column(name = "data_cessazione")
    private Instant dataCessazione;

    @NotNull
    @Column(name = "intestatario_contratto", nullable = false)
    private String intestatarioContratto;

    @Column(name = "numero_contratto")
    private String numeroContratto;

    @Column(name = "utilizzatore_utenza")
    private String utilizzatoreUtenza;

    @Column(name = "cdsuo")
    private String cdsuo;

    @Lob
    @Column(name = "operatore")
    private String operatore;

    @Lob
    @Column(name = "servizi")
    private String servizi;

    @NotNull
    @Column(name = "user_modifica", nullable = false)
    private String userModifica;

    @Column(name = "versione")
    private String versione;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Telefono storicotelefonoTelefono;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataModifica() {
        return dataModifica;
    }

    public StoricoTelefono dataModifica(Instant dataModifica) {
        this.dataModifica = dataModifica;
        return this;
    }

    public void setDataModifica(Instant dataModifica) {
        this.dataModifica = dataModifica;
    }

    public Instant getDataAttivazione() {
        return dataAttivazione;
    }

    public StoricoTelefono dataAttivazione(Instant dataAttivazione) {
        this.dataAttivazione = dataAttivazione;
        return this;
    }

    public void setDataAttivazione(Instant dataAttivazione) {
        this.dataAttivazione = dataAttivazione;
    }

    public Instant getDataCessazione() {
        return dataCessazione;
    }

    public StoricoTelefono dataCessazione(Instant dataCessazione) {
        this.dataCessazione = dataCessazione;
        return this;
    }

    public void setDataCessazione(Instant dataCessazione) {
        this.dataCessazione = dataCessazione;
    }

    public String getIntestatarioContratto() {
        return intestatarioContratto;
    }

    public StoricoTelefono intestatarioContratto(String intestatarioContratto) {
        this.intestatarioContratto = intestatarioContratto;
        return this;
    }

    public void setIntestatarioContratto(String intestatarioContratto) {
        this.intestatarioContratto = intestatarioContratto;
    }

    public String getNumeroContratto() {
        return numeroContratto;
    }

    public StoricoTelefono numeroContratto(String numeroContratto) {
        this.numeroContratto = numeroContratto;
        return this;
    }

    public void setNumeroContratto(String numeroContratto) {
        this.numeroContratto = numeroContratto;
    }

    public String getUtilizzatoreUtenza() {
        return utilizzatoreUtenza;
    }

    public StoricoTelefono utilizzatoreUtenza(String utilizzatoreUtenza) {
        this.utilizzatoreUtenza = utilizzatoreUtenza;
        return this;
    }

    public void setUtilizzatoreUtenza(String utilizzatoreUtenza) {
        this.utilizzatoreUtenza = utilizzatoreUtenza;
    }

    public String getCdsuo() {
        return cdsuo;
    }

    public StoricoTelefono cdsuo(String cdsuo) {
        this.cdsuo = cdsuo;
        return this;
    }

    public void setCdsuo(String cdsuo) {
        this.cdsuo = cdsuo;
    }

    public String getOperatore() {
        return operatore;
    }

    public StoricoTelefono operatore(String operatore) {
        this.operatore = operatore;
        return this;
    }

    public void setOperatore(String operatore) {
        this.operatore = operatore;
    }

    public String getServizi() {
        return servizi;
    }

    public StoricoTelefono servizi(String servizi) {
        this.servizi = servizi;
        return this;
    }

    public void setServizi(String servizi) {
        this.servizi = servizi;
    }

    public String getUserModifica() {
        return userModifica;
    }

    public StoricoTelefono userModifica(String userModifica) {
        this.userModifica = userModifica;
        return this;
    }

    public void setUserModifica(String userModifica) {
        this.userModifica = userModifica;
    }

    public String getVersione() {
        return versione;
    }

    public StoricoTelefono versione(String versione) {
        this.versione = versione;
        return this;
    }

    public void setVersione(String versione) {
        this.versione = versione;
    }

    public Telefono getStoricotelefonoTelefono() {
        return storicotelefonoTelefono;
    }

    public StoricoTelefono storicotelefonoTelefono(Telefono telefono) {
        this.storicotelefonoTelefono = telefono;
        return this;
    }

    public void setStoricotelefonoTelefono(Telefono telefono) {
        this.storicotelefonoTelefono = telefono;
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
        StoricoTelefono storicoTelefono = (StoricoTelefono) o;
        if (storicoTelefono.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), storicoTelefono.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StoricoTelefono{" +
            "id=" + getId() +
            ", dataModifica='" + getDataModifica() + "'" +
            ", dataAttivazione='" + getDataAttivazione() + "'" +
            ", dataCessazione='" + getDataCessazione() + "'" +
            ", intestatarioContratto='" + getIntestatarioContratto() + "'" +
            ", numeroContratto='" + getNumeroContratto() + "'" +
            ", utilizzatoreUtenza='" + getUtilizzatoreUtenza() + "'" +
            ", cdsuo='" + getCdsuo() + "'" +
            ", operatore='" + getOperatore() + "'" +
            ", servizi='" + getServizi() + "'" +
            ", userModifica='" + getUserModifica() + "'" +
            ", versione='" + getVersione() + "'" +
            "}";
    }
}
