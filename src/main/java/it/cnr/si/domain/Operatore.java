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
import io.swagger.models.auth.In;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
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
    private Instant data;

    @Column(name = "data_fine")
    private Instant dataFine;

    @NotNull
    @Column(name = "numero_contratto", nullable = false)
    private String numeroContratto;


    @Lob
    @Column(name = "contratto", nullable = false)
    private byte[] contratto;

    @Column(name = "contratto_content_type", nullable = false)
    private String contrattoContentType;

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

    public Instant getData() {
        return data;
    }

    public Operatore data(Instant data) {
        this.data = data;
        return this;
    }

    public void setData(Instant data) {
        this.data = data;
    }

    public Instant getDataFine() {
        return dataFine;
    }

    public Operatore dataFine(Instant dataFine) {
        this.dataFine = dataFine;
        return this;
    }

    public void setDataFine(Instant dataFine) {
        this.dataFine = dataFine;
    }

    public String getNumeroContratto() {
        return numeroContratto;
    }

    public Operatore numeroContratto(String numeroContratto) {
        this.numeroContratto = numeroContratto;
        return this;
    }

    public void setNumeroContratto(String numeroContratto) {
        this.numeroContratto = numeroContratto;
    }

    public byte[] getContratto() {
        return contratto;
    }

    public Operatore contratto(byte[] contratto) {
        this.contratto = contratto;
        return this;
    }

    public void setContratto(byte[] contratto) {
        this.contratto = contratto;
    }

    public String getContrattoContentType() {
        return contrattoContentType;
    }

    public Operatore contrattoContentType(String contrattoContentType) {
        this.contrattoContentType = contrattoContentType;
        return this;
    }

    public void setContrattoContentType(String contrattoContentType) {
        this.contrattoContentType = contrattoContentType;
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
            ", dataFine='" + getDataFine() + "'" +
            ", numeroContratto='" + getNumeroContratto() + "'" +
            ", contratto='" + getContratto() + "'" +
            ", contrattoContentType='" + getContrattoContentType() + "'" +
            "}";
    }
}
