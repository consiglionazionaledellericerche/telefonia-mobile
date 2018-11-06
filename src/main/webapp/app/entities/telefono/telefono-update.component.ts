import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ITelefono } from 'app/shared/model/telefono.model';
import { TelefonoService } from './telefono.service';
import { IUtenza } from 'app/shared/model/utenza.model';
import { UtenzaService } from 'app/entities/utenza';
import { IIstituto } from 'app/shared/model/istituto.model';
import { IstitutoService } from 'app/entities/istituto';

@Component({
    selector: 'jhi-telefono-update',
    templateUrl: './telefono-update.component.html'
})
export class TelefonoUpdateComponent implements OnInit {
    private _telefono: ITelefono;
    isSaving: boolean;

    utenzas: IUtenza[];

    istitutos: IIstituto[];
    dataattivazioneDp: any;
    datacessazioneDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private telefonoService: TelefonoService,
        private utenzaService: UtenzaService,
        private istitutoService: IstitutoService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ telefono }) => {
            this.telefono = telefono;
        });
        this.utenzaService.query().subscribe(
            (res: HttpResponse<IUtenza[]>) => {
                this.utenzas = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.istitutoService.query().subscribe(
            (res: HttpResponse<IIstituto[]>) => {
                this.istitutos = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.telefono.id !== undefined) {
            this.subscribeToSaveResponse(this.telefonoService.update(this.telefono));
        } else {
            this.subscribeToSaveResponse(this.telefonoService.create(this.telefono));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITelefono>>) {
        result.subscribe((res: HttpResponse<ITelefono>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUtenzaById(index: number, item: IUtenza) {
        return item.id;
    }

    trackIstitutoById(index: number, item: IIstituto) {
        return item.id;
    }
    get telefono() {
        return this._telefono;
    }

    set telefono(telefono: ITelefono) {
        this._telefono = telefono;
    }
}
