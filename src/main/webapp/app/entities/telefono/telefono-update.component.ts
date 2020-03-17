import { Component, OnInit, Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ITelefono } from 'app/shared/model/telefono.model';
import { TelefonoService } from './telefono.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, map, tap, switchMap } from 'rxjs/operators';
import { JhiDataUtils } from 'ng-jhipster';

const WIKI_URL = 'https://en.wikipedia.org/w/api.php';
const PARAMS = new HttpParams({
    fromObject: {
        action: 'opensearch',
        format: 'json',
        origin: '*'
    }
});

@Injectable()
export class WikipediaService {
    constructor(private http: HttpClient) {}

    search(term: string) {
        if (term === '') {
            return of([]);
        }

        return this.http.get(WIKI_URL, { params: PARAMS.set('search', term) }).pipe(map(response => response[1]));
    }
}

@Component({
    selector: 'jhi-telefono-update',
    providers: [WikipediaService],
    templateUrl: './telefono-update.component.html'
})
export class TelefonoUpdateComponent implements OnInit {
    model: any;
    private _telefono: ITelefono;
    isSaving: boolean;
    dataAttivazioneDp: any;
    dataCessazioneDp: any;
    searching = false;
    searchFailed = false;
    istituti = [];

    constructor(
        private _service: WikipediaService,
        private dataUtils: JhiDataUtils,
        private telefonoService: TelefonoService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ telefono }) => {
            this.telefono = telefono;
        });
        this.telefonoService.getIstituti().subscribe(istitutiRestituiti => {
            this.istituti = istitutiRestituiti;
        });
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

    getIstitutoDescr(istituto: any) {
        return (
            istituto.cdsuo +
            ' - ' +
            istituto.denominazione +
            (istituto.indirizzoPrincipale ? ' (' + istituto.indirizzoPrincipale.comune + ')' : '')
        );
    }

    selectIstituto() {
        this._telefono.cdsuo = this.istituti.filter(value => value.cdsuo === this._telefono.intestatarioContratto)[0]['denominazione'];
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
            tap(() => (this.searching = true)),
            switchMap(term =>
                this.telefonoService.findPersona(term).pipe(
                    //        this._service.search(term).pipe(
                    tap(() => (this.searchFailed = false)),
                    catchError(() => {
                        this.searchFailed = true;
                        return of([]);
                    })
                )
            ),
            tap(() => (this.searching = false))
        );
}
