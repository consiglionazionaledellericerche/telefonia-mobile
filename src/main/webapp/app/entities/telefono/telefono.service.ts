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
            dataattivazione:
                telefono.dataattivazione != null && telefono.dataattivazione.isValid()
                    ? telefono.dataattivazione.format(DATE_FORMAT)
                    : null,
            datacessazione:
                telefono.datacessazione != null && telefono.datacessazione.isValid() ? telefono.datacessazione.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dataattivazione = res.body.dataattivazione != null ? moment(res.body.dataattivazione) : null;
        res.body.datacessazione = res.body.datacessazione != null ? moment(res.body.datacessazione) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((telefono: ITelefono) => {
            telefono.dataattivazione = telefono.dataattivazione != null ? moment(telefono.dataattivazione) : null;
            telefono.datacessazione = telefono.datacessazione != null ? moment(telefono.datacessazione) : null;
        });
        return res;
    }
}
