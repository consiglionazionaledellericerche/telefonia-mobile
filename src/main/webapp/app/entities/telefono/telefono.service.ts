import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITelefono } from 'app/shared/model/telefono.model';

type EntityResponseType = HttpResponse<ITelefono>;
type EntityArrayResponseType = HttpResponse<ITelefono[]>;

@Injectable({ providedIn: 'root' })
export class TelefonoService {
    private resourceUrl = SERVER_API_URL + 'api/telefonos';

    constructor(private http: HttpClient) {}

    create(telefono: ITelefono): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(telefono);
        return this.http
            .post<ITelefono>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(telefono: ITelefono): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(telefono);
        return this.http
            .put<ITelefono>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ITelefono>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITelefono[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(telefono: ITelefono): ITelefono {
        const copy: ITelefono = Object.assign({}, telefono, {
            dataAttivazione:
                telefono.dataAttivazione != null && telefono.dataAttivazione.isValid()
                    ? telefono.dataAttivazione.format(DATE_FORMAT)
                    : null,
            dataCessazione:
                telefono.dataCessazione != null && telefono.dataCessazione.isValid() ? telefono.dataCessazione.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dataAttivazione = res.body.dataAttivazione != null ? moment(res.body.dataAttivazione) : null;
        res.body.dataCessazione = res.body.dataCessazione != null ? moment(res.body.dataCessazione) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((telefono: ITelefono) => {
            telefono.dataAttivazione = telefono.dataAttivazione != null ? moment(telefono.dataAttivazione) : null;
            telefono.dataCessazione = telefono.dataCessazione != null ? moment(telefono.dataCessazione) : null;
        });
        return res;
    }

#    per utenza ACE
    findPersona(term: string): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/findUtenza/${term}`, { observe: 'response' });
    }

}
