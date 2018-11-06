import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ITelefonoservizi } from 'app/shared/model/telefonoservizi.model';
import { TelefonoserviziService } from './telefonoservizi.service';
import { ITelefono } from 'app/shared/model/telefono.model';
import { TelefonoService } from 'app/entities/telefono';
import { IServizi } from 'app/shared/model/servizi.model';
import { ServiziService } from 'app/entities/servizi';

@Component({
    selector: 'jhi-telefonoservizi-update',
    templateUrl: './telefonoservizi-update.component.html'
})
export class TelefonoserviziUpdateComponent implements OnInit {
    private _telefonoservizi: ITelefonoservizi;
    isSaving: boolean;

    telefonos: ITelefono[];

    servizis: IServizi[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private telefonoserviziService: TelefonoserviziService,
        private telefonoService: TelefonoService,
        private serviziService: ServiziService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ telefonoservizi }) => {
            this.telefonoservizi = telefonoservizi;
        });
        this.telefonoService.query().subscribe(
            (res: HttpResponse<ITelefono[]>) => {
                this.telefonos = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.serviziService.query().subscribe(
            (res: HttpResponse<IServizi[]>) => {
                this.servizis = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.telefonoservizi.id !== undefined) {
            this.subscribeToSaveResponse(this.telefonoserviziService.update(this.telefonoservizi));
        } else {
            this.subscribeToSaveResponse(this.telefonoserviziService.create(this.telefonoservizi));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITelefonoservizi>>) {
        result.subscribe((res: HttpResponse<ITelefonoservizi>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackServiziById(index: number, item: IServizi) {
        return item.id;
    }
    get telefonoservizi() {
        return this._telefonoservizi;
    }

    set telefonoservizi(telefonoservizi: ITelefonoservizi) {
        this._telefonoservizi = telefonoservizi;
    }
}
