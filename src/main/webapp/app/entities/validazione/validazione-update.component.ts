import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IValidazione } from 'app/shared/model/validazione.model';
import { ValidazioneService } from './validazione.service';
import { ITelefono } from 'app/shared/model/telefono.model';
import { TelefonoService } from 'app/entities/telefono';
import { IStoricoTelefono } from 'app/shared/model/storico-telefono.model';
import { StoricoTelefonoService } from 'app/entities/storico-telefono';

@Component({
    selector: 'jhi-validazione-update',
    templateUrl: './validazione-update.component.html'
})
export class ValidazioneUpdateComponent implements OnInit {
    private _validazione: IValidazione;
    isSaving: boolean;

    telefonos: ITelefono[];

    stampas: IStoricoTelefono[];
    dataModificaDp: any;
    dataValidazione: string;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private validazioneService: ValidazioneService,
        private telefonoService: TelefonoService,
        private storicoTelefonoService: StoricoTelefonoService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ validazione }) => {
            this.validazione = validazione;
        });
        this.telefonoService.query().subscribe(
            (res: HttpResponse<ITelefono[]>) => {
                this.telefonos = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.storicoTelefonoService.query({ filter: 'validazione-is-null' }).subscribe(
            (res: HttpResponse<IStoricoTelefono[]>) => {
                if (!this.validazione.stampa || !this.validazione.stampa.id) {
                    this.stampas = res.body;
                } else {
                    this.storicoTelefonoService.find(this.validazione.stampa.id).subscribe(
                        (subRes: HttpResponse<IStoricoTelefono>) => {
                            this.stampas = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.validazione.dataValidazione = moment(this.dataValidazione, DATE_TIME_FORMAT);
        if (this.validazione.id !== undefined) {
            this.subscribeToSaveResponse(this.validazioneService.update(this.validazione));
        } else {
            this.subscribeToSaveResponse(this.validazioneService.create(this.validazione));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IValidazione>>) {
        result.subscribe((res: HttpResponse<IValidazione>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackTelefonoById(index: number, item: ITelefono) {
        return item.id;
    }

    trackStoricoTelefonoById(index: number, item: IStoricoTelefono) {
        return item.id;
    }
    get validazione() {
        return this._validazione;
    }

    set validazione(validazione: IValidazione) {
        this._validazione = validazione;
        this.dataValidazione = moment(validazione.dataValidazione).format(DATE_TIME_FORMAT);
    }
}
