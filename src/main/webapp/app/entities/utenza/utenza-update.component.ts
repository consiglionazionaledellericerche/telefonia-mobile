import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IUtenza } from 'app/shared/model/utenza.model';
import { UtenzaService } from './utenza.service';
import { IUser, UserService } from 'app/core';
import { IIstituto } from 'app/shared/model/istituto.model';
import { IstitutoService } from 'app/entities/istituto';

@Component({
    selector: 'jhi-utenza-update',
    templateUrl: './utenza-update.component.html'
})
export class UtenzaUpdateComponent implements OnInit {
    private _utenza: IUtenza;
    isSaving: boolean;

    users: IUser[];

    istitutos: IIstituto[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private utenzaService: UtenzaService,
        private userService: UserService,
        private istitutoService: IstitutoService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ utenza }) => {
            this.utenza = utenza;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
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
        if (this.utenza.id !== undefined) {
            this.subscribeToSaveResponse(this.utenzaService.update(this.utenza));
        } else {
            this.subscribeToSaveResponse(this.utenzaService.create(this.utenza));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IUtenza>>) {
        result.subscribe((res: HttpResponse<IUtenza>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackIstitutoById(index: number, item: IIstituto) {
        return item.id;
    }
    get utenza() {
        return this._utenza;
    }

    set utenza(utenza: IUtenza) {
        this._utenza = utenza;
    }
}
