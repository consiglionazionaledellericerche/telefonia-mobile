import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
//import { Observable } from 'rxjs';

import { ITelefono } from 'app/shared/model/telefono.model';
import { TelefonoService } from './telefono.service';


//import {Component, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError, debounceTime, distinctUntilChanged, map, tap, switchMap} from 'rxjs/operators';

@Component({
    selector: 'jhi-telefono-update',
    templateUrl: './telefono-update.component.html'
})
export class TelefonoUpdateComponent implements OnInit {
    private _telefono: ITelefono;
    isSaving: boolean;
    dataAttivazioneDp: any;
    dataCessazioneDp: any;

    constructor(private telefonoService: TelefonoService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ telefono }) => {
            this.telefono = telefono;
        });
    }

    previousState() {
        window.history.back();
    }

  searching = false;
  searchFailed = false;

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
    get telefono() {
        return this._telefono;
    }

    set telefono(telefono: ITelefono) {
        this._telefono = telefono;
    }

  search = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => this.searching = true),
      switchMap(term =>
        this.telefonoService.findPersona(term).pipe(
          tap(() => this.searchFailed = false),
          catchError(() => {
            this.searchFailed = true;
            return of([]);
          }))
      ),
      tap(() => this.searching = false)
)

formatter = (x: {username: string}) => x.username
}
