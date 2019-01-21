import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ITelefonoServizi } from 'app/shared/model/telefono-servizi.model';
import { TelefonoServiziService } from './telefono-servizi.service';
import { IServizi } from 'app/shared/model/servizi.model';
import { ServiziService } from 'app/entities/servizi';
import { ITelefono } from 'app/shared/model/telefono.model';
import { TelefonoService } from 'app/entities/telefono';

@Component({
    selector: 'jhi-telefono-servizi-update',
    templateUrl: './telefono-servizi-update.component.html'
})
export class TelefonoServiziUpdateComponent implements OnInit {
    private _telefonoServizi: ITelefonoServizi;
    isSaving: boolean;
    telefono = [];

    servizis: IServizi[];

    telefonos: ITelefono[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private telefonoServiziService: TelefonoServiziService,
        private serviziService: ServiziService,
        private telefonoService: TelefonoService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ telefonoServizi }) => {
            this.telefonoServizi = telefonoServizi;
        });
        this.serviziService.query().subscribe(
            (res: HttpResponse<IServizi[]>) => {
                this.servizis = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.telefonoService.query().subscribe(
            (res: HttpResponse<ITelefono[]>) => {
                this.telefonos = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );

        this.telefonoServiziService.findTelefono().subscribe(telefonoRestituiti => {
            this.telefono = telefonoRestituiti;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.telefonoServizi.id !== undefined) {
            this.subscribeToSaveResponse(this.telefonoServiziService.update(this.telefonoServizi));
        } else {
            this.subscribeToSaveResponse(this.telefonoServiziService.create(this.telefonoServizi));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITelefonoServizi>>) {
        result.subscribe((res: HttpResponse<ITelefonoServizi>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackServiziById(index: number, item: IServizi) {
        return item.id;
    }

    trackTelefonoById(index: number, item: ITelefono) {
        return item.id;
    }
    get telefonoServizi() {
        return this._telefonoServizi;
    }

    set telefonoServizi(telefonoServizi: ITelefonoServizi) {
        this._telefonoServizi = telefonoServizi;
    }
}
