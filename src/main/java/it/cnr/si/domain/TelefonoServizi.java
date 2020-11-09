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
 * A TelefonoServizi.
 */
@Entity
@Table(name = "telefono_servizi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TelefonoServizi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "altro")
    private String altro;

    @NotNull
    @Column(name = "data_inizio", nullable = false)
    private Instant dataInizio;

    @NotNull
    @Column(name = "data_fine", nullable = false)
    private Instant dataFine;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Servizi servizi;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Telefono telefono;

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

    public TelefonoServizi altro(String altro) {
        this.altro = altro;
        return this;
    }

    public void setAltro(String altro) {
        this.altro = altro;
    }

    public Instant getDataInizio() {
        return dataInizio;
    }

    public TelefonoServizi dataInizio(Instant dataInizio) {
        this.dataInizio = dataInizio;
        return this;
    }

    public void setDataInizio(Instant dataInizio) {
        this.dataInizio = dataInizio;
    }

    public Instant getDataFine() {
        return dataFine;
    }

    public TelefonoServizi dataFine(Instant dataFine) {
        this.dataFine = dataFine;
        return this;
    }

    public void setDataFine(Instant dataFine) {
        this.dataFine = dataFine;
    }

    public Servizi getServizi() {
        return servizi;
    }

    public TelefonoServizi servizi(Servizi servizi) {
        this.servizi = servizi;
        return this;
    }

    public void setServizi(Servizi servizi) {
        this.servizi = servizi;
    }

    public Telefono getTelefono() {
        return telefono;
    }

    public TelefonoServizi telefono(Telefono telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(Telefono telefono) {
        this.telefono = telefono;
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
        TelefonoServizi telefonoServizi = (TelefonoServizi) o;
        if (telefonoServizi.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), telefonoServizi.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TelefonoServizi{" +
            "id=" + getId() +
            ", altro='" + getAltro() + "'" +
            ", dataInizio='" + getDataInizio() + "'" +
            ", dataFine='" + getDataFine() + "'" +
            "}";
    }
}
