import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IListaOperatori } from 'app/shared/model/lista-operatori.model';
import { ListaOperatoriService } from './lista-operatori.service';

@Component({
    selector: 'jhi-lista-operatori-update',
    templateUrl: './lista-operatori-update.component.html'
})
export class ListaOperatoriUpdateComponent implements OnInit {
    private _listaOperatori: IListaOperatori;
    isSaving: boolean;

    constructor(private listaOperatoriService: ListaOperatoriService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ listaOperatori }) => {
            this.listaOperatori = listaOperatori;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.listaOperatori.id !== undefined) {
            this.subscribeToSaveResponse(this.listaOperatoriService.update(this.listaOperatori));
        } else {
            this.subscribeToSaveResponse(this.listaOperatoriService.create(this.listaOperatori));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IListaOperatori>>) {
        result.subscribe((res: HttpResponse<IListaOperatori>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get listaOperatori() {
        return this._listaOperatori;
    }

    set listaOperatori(listaOperatori: IListaOperatori) {
        this._listaOperatori = listaOperatori;
    }
}
