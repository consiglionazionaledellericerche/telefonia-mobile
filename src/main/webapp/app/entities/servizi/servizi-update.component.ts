import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IServizi } from 'app/shared/model/servizi.model';
import { ServiziService } from './servizi.service';

@Component({
    selector: 'jhi-servizi-update',
    templateUrl: './servizi-update.component.html'
})
export class ServiziUpdateComponent implements OnInit {
    private _servizi: IServizi;
    isSaving: boolean;

    constructor(private serviziService: ServiziService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ servizi }) => {
            this.servizi = servizi;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.servizi.id !== undefined) {
            this.subscribeToSaveResponse(this.serviziService.update(this.servizi));
        } else {
            this.subscribeToSaveResponse(this.serviziService.create(this.servizi));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IServizi>>) {
        result.subscribe((res: HttpResponse<IServizi>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get servizi() {
        return this._servizi;
    }

    set servizi(servizi: IServizi) {
        this._servizi = servizi;
    }
}
