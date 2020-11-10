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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
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
    private Instant dataAttivazione;

    @Column(name = "data_cessazione")
    private Instant dataCessazione;

    @NotNull
    @Column(name = "intestatario_contratto", nullable = false)
    private String intestatarioContratto;

    @Column(name = "numero_contratto")
    private String numeroContratto;

    @NotNull
    @Column(name = "cdsuo", nullable = false)
    private String cdsuo;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "deleted_note")
    private String deletedNote;

    @NotNull
    @Column(name = "utilizzatore_utenza", nullable = false)
    private String utilizzatoreUtenza;


    @Lob
    @Column(name = "contratto")
    private byte[] contratto;

    @Column(name = "contratto_content_type")
    private String contrattoContentType;

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

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Telefono numero(String numero) {
        this.numero = numero;
        return this;
    }

    public Instant getDataAttivazione() {
        return dataAttivazione;
    }

    public void setDataAttivazione(Instant dataAttivazione) {
        this.dataAttivazione = dataAttivazione;
    }

    public Telefono dataAttivazione(Instant dataAttivazione) {
        this.dataAttivazione = dataAttivazione;
        return this;
    }

    public Instant getDataCessazione() {
        return dataCessazione;
    }

    public void setDataCessazione(Instant dataCessazione) {
        this.dataCessazione = dataCessazione;
    }

    public Telefono dataCessazione(Instant dataCessazione) {
        this.dataCessazione = dataCessazione;
        return this;
    }

    public String getIntestatarioContratto() {
        return intestatarioContratto;
    }

    public void setIntestatarioContratto(String intestatarioContratto) {
        this.intestatarioContratto = intestatarioContratto;
    }

    public Telefono intestatarioContratto(String intestatarioContratto) {
        this.intestatarioContratto = intestatarioContratto;
        return this;
    }

    public String getNumeroContratto() {
        return numeroContratto;
    }

    public void setNumeroContratto(String numeroContratto) {
        this.numeroContratto = numeroContratto;
    }

    public Telefono numeroContratto(String numeroContratto) {
        this.numeroContratto = numeroContratto;
        return this;
    }

    public String getCdsuo() {
        return cdsuo;
    }

    public void setCdsuo(String cdsuo) {
        this.cdsuo = cdsuo;
    }

    public Telefono cdsuo(String cdsuo) {
        this.cdsuo = cdsuo;
        return this;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Telefono deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDeletedNote() {
        return deletedNote;
    }

    public void setDeletedNote(String deletedNote) {
        this.deletedNote = deletedNote;
    }

    public Telefono deletedNote(String deletedNote) {
        this.deletedNote = deletedNote;
        return this;
    }

    public String getUtilizzatoreUtenza() {
        return utilizzatoreUtenza;
    }

    public void setUtilizzatoreUtenza(String utilizzatoreUtenza) {
        this.utilizzatoreUtenza = utilizzatoreUtenza;
    }

    public Telefono utilizzatoreUtenza(String utilizzatoreUtenza) {
        this.utilizzatoreUtenza = utilizzatoreUtenza;
        return this;
    }

    public byte[] getContratto() {
        return contratto;
    }

    public void setContratto(byte[] contratto) {
        this.contratto = contratto;
    }

    public Telefono contratto(byte[] contratto) {
        this.contratto = contratto;
        return this;
    }

    public String getContrattoContentType() {
        return contrattoContentType;
    }

    public void setContrattoContentType(String contrattoContentType) {
        this.contrattoContentType = contrattoContentType;
    }

    public Telefono contrattoContentType(String contrattoContentType) {
        this.contrattoContentType = contrattoContentType;
        return this;
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
            ", cdsuo='" + getCdsuo() + "'" +
            ", deleted='" + isDeleted() + "'" +
            ", deletedNote='" + getDeletedNote() + "'" +
            ", utilizzatoreUtenza='" + getUtilizzatoreUtenza() + "'" +
            ", contratto='" + getContratto() + "'" +
            ", contrattoContentType='" + getContrattoContentType() + "'" +
            "}";
    }
}
