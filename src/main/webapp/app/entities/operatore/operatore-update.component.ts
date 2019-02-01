import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IOperatore } from 'app/shared/model/operatore.model';
import { OperatoreService } from './operatore.service';
import { ITelefono } from 'app/shared/model/telefono.model';
import { TelefonoService } from 'app/entities/telefono';
import { IListaOperatori } from 'app/shared/model/lista-operatori.model';
import { ListaOperatoriService } from 'app/entities/lista-operatori';

@Component({
    selector: 'jhi-operatore-update',
    templateUrl: './operatore-update.component.html'
})
export class OperatoreUpdateComponent implements OnInit {
    private _operatore: IOperatore;
    isSaving: boolean;

    telefonos: ITelefono[];

    listaoperatoris: IListaOperatori[];
    dataDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private operatoreService: OperatoreService,
        private telefonoService: TelefonoService,
        private listaOperatoriService: ListaOperatoriService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ operatore }) => {
            this.operatore = operatore;
        });
        this.telefonoService.query().subscribe(
            (res: HttpResponse<ITelefono[]>) => {
                this.telefonos = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.listaOperatoriService.query().subscribe(
            (res: HttpResponse<IListaOperatori[]>) => {
                this.listaoperatoris = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.operatore.id !== undefined) {
            this.subscribeToSaveResponse(this.operatoreService.update(this.operatore));
        } else {
            this.subscribeToSaveResponse(this.operatoreService.create(this.operatore));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IOperatore>>) {
        result.subscribe((res: HttpResponse<IOperatore>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackListaOperatoriById(index: number, item: IListaOperatori) {
        return item.id;
    }
    get operatore() {
        return this._operatore;
    }

    set operatore(operatore: IOperatore) {
        this._operatore = operatore;
    }
}
