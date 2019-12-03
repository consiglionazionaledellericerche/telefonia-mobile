import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IStoricoTelefono } from 'app/shared/model/storico-telefono.model';
import { StoricoTelefonoService } from './storico-telefono.service';
import { ITelefono } from 'app/shared/model/telefono.model';
import { TelefonoService } from 'app/entities/telefono';

@Component({
    selector: 'jhi-storico-telefono-update',
    templateUrl: './storico-telefono-update.component.html'
})
export class StoricoTelefonoUpdateComponent implements OnInit {
    private _storicoTelefono: IStoricoTelefono;
    isSaving: boolean;

    telefonos: ITelefono[];
    dataModifica: string;
    dataAttivazioneDp: any;
    dataCessazioneDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private storicoTelefonoService: StoricoTelefonoService,
        private telefonoService: TelefonoService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ storicoTelefono }) => {
            this.storicoTelefono = storicoTelefono;
        });
        this.telefonoService.query().subscribe(
            (res: HttpResponse<ITelefono[]>) => {
                this.telefonos = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.storicoTelefono.dataModifica = moment(this.dataModifica, DATE_TIME_FORMAT);
        if (this.storicoTelefono.id !== undefined) {
            this.subscribeToSaveResponse(this.storicoTelefonoService.update(this.storicoTelefono));
        } else {
            this.subscribeToSaveResponse(this.storicoTelefonoService.create(this.storicoTelefono));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IStoricoTelefono>>) {
        result.subscribe((res: HttpResponse<IStoricoTelefono>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get storicoTelefono() {
        return this._storicoTelefono;
    }

    set storicoTelefono(storicoTelefono: IStoricoTelefono) {
        this._storicoTelefono = storicoTelefono;
        this.dataModifica = moment(storicoTelefono.dataModifica).format(DATE_TIME_FORMAT);
    }
}
