import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITelefonoServizi } from 'app/shared/model/telefono-servizi.model';

type EntityResponseType = HttpResponse<ITelefonoServizi>;
type EntityArrayResponseType = HttpResponse<ITelefonoServizi[]>;

@Injectable({ providedIn: 'root' })
export class TelefonoServiziService {
    private resourceUrl = SERVER_API_URL + 'api/telefono-servizis';

    constructor(private http: HttpClient) {}

    create(telefonoServizi: ITelefonoServizi): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(telefonoServizi);
        return this.http
            .post<ITelefonoServizi>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(telefonoServizi: ITelefonoServizi): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(telefonoServizi);
        return this.http
            .put<ITelefonoServizi>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ITelefonoServizi>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITelefonoServizi[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(telefonoServizi: ITelefonoServizi): ITelefonoServizi {
        const copy: ITelefonoServizi = Object.assign({}, telefonoServizi, {
            dataInizio:
                telefonoServizi.dataInizio != null && telefonoServizi.dataInizio.isValid() ? telefonoServizi.dataInizio.toJSON() : null,
            dataFine: telefonoServizi.dataFine != null && telefonoServizi.dataFine.isValid() ? telefonoServizi.dataFine.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dataInizio = res.body.dataInizio != null ? moment(res.body.dataInizio) : null;
        res.body.dataFine = res.body.dataFine != null ? moment(res.body.dataFine) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((telefonoServizi: ITelefonoServizi) => {
            telefonoServizi.dataInizio = telefonoServizi.dataInizio != null ? moment(telefonoServizi.dataInizio) : null;
            telefonoServizi.dataFine = telefonoServizi.dataFine != null ? moment(telefonoServizi.dataFine) : null;
        });
        return res;
    }

    findTelefono() {
        return this.http.get<any>(`${this.resourceUrl}/findTelefono`);
    }
}
