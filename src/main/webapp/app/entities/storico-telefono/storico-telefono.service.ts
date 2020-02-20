import { Injectable, Component } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';
import { saveAs } from 'file-saver';
import { JhiDataUtils } from 'ng-jhipster';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IStoricoTelefono } from 'app/shared/model/storico-telefono.model';
import { saveAs as importedSaveAs } from 'file-saver';

type EntityResponseType = HttpResponse<IStoricoTelefono>;
type EntityArrayResponseType = HttpResponse<IStoricoTelefono[]>;

@Injectable({ providedIn: 'root' })
export class StoricoTelefonoService {
    private resourceUrl = SERVER_API_URL + 'api/storico-telefonos';
    private resourceVistaUrl = SERVER_API_URL + 'api/storico-telefonos/vista';

    constructor(private http: HttpClient, private dataUtils: JhiDataUtils) {}

    create(storicoTelefono: IStoricoTelefono): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(storicoTelefono);
        return this.http
            .post<IStoricoTelefono>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(storicoTelefono: IStoricoTelefono): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(storicoTelefono);
        return this.http
            .put<IStoricoTelefono>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IStoricoTelefono>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IStoricoTelefono[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    queryVista(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IStoricoTelefono[]>(this.resourceVistaUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    //     getPdf() {
    //       return this.http.get<any>(`${this.resourceUrl}/pdf`);
    // }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(storicoTelefono: IStoricoTelefono): IStoricoTelefono {
        const copy: IStoricoTelefono = Object.assign({}, storicoTelefono, {
            dataModifica:
                storicoTelefono.dataModifica != null && storicoTelefono.dataModifica.isValid()
                    ? storicoTelefono.dataModifica.toJSON()
                    : null,
            dataAttivazione:
                storicoTelefono.dataAttivazione != null && storicoTelefono.dataAttivazione.isValid()
                    ? storicoTelefono.dataAttivazione.format(DATE_FORMAT)
                    : null,
            dataCessazione:
                storicoTelefono.dataCessazione != null && storicoTelefono.dataCessazione.isValid()
                    ? storicoTelefono.dataCessazione.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dataModifica = res.body.dataModifica != null ? moment(res.body.dataModifica) : null;
        res.body.dataAttivazione = res.body.dataAttivazione != null ? moment(res.body.dataAttivazione) : null;
        res.body.dataCessazione = res.body.dataCessazione != null ? moment(res.body.dataCessazione) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((storicoTelefono: IStoricoTelefono) => {
            storicoTelefono.dataModifica = storicoTelefono.dataModifica != null ? moment(storicoTelefono.dataModifica) : null;
            storicoTelefono.dataAttivazione = storicoTelefono.dataAttivazione != null ? moment(storicoTelefono.dataAttivazione) : null;
            storicoTelefono.dataCessazione = storicoTelefono.dataCessazione != null ? moment(storicoTelefono.dataCessazione) : null;
        });
        return res;
    }

    pdf() {
        const pluto = this.http
            .get('api/storico-telefonos/pdf')
            .map(res => res.blob())
            .catch(this.handleError);

        pluto.subscribe(blob => {
            importedSaveAs(blob, this.fileName);
        });

        const FileSaver = require('file-saver');
        // const options = {responseType: ResponseContentType.ArrayBuffer };
        // const pippo = this.http.get<any>('api/storico-telefono/pdf');
        // const blob = new Blob(pippo, {type: 'application/pdf'});
        const pippo = this.http.get('api/storico-telefonos/pdf', 'application/pdf');
        FileSaver.saveAs('api/storico-telefonos/pdf', 'pdf.pdf');
        // this.http.get<IStoricoTelefono>('${this.resourceUrl}/pdf')
        // const FileSaver = require('file-saver');
        // this.dataUtils.openFile('application/pdf', '/documenti/pdf.pdf');
        // this.http.get<IStoricoTelefono>('${this.resourceUrl}/pdf')
        // .pipe(
        //       map((data: pdff) => {
        //           const blob = new Blob(pdff, {type: 'application/pdf'});
        //           FileSaver.saveAs(blob, 'pdf.pdf');
        //       }), catchError( error => {
        //           return throwError( 'Something went wrong!' );
        //       })
        // )
        // const pippo = this.http.get<IStoricoTelefono>(`${this.resourceUrl}/pdf`);
        // const blob = new Blob([pippo], {type: 'application/pdf'});
        // FileSaver.saveAs(blob, 'pdf.pdf');
    }
}
