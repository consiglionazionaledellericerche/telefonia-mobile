import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IValidazione } from 'app/shared/model/validazione.model';

type EntityResponseType = HttpResponse<IValidazione>;
type EntityArrayResponseType = HttpResponse<IValidazione[]>;

@Injectable({ providedIn: 'root' })
export class ValidazioneService {
    private resourceUrl = SERVER_API_URL + 'api/validaziones';

    constructor(private http: HttpClient) {}

    create(validazione: IValidazione): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(validazione);
        return this.http
            .post<IValidazione>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(validazione: IValidazione): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(validazione);
        return this.http
            .put<IValidazione>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IValidazione>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IValidazione[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(validazione: IValidazione): IValidazione {
        const copy: IValidazione = Object.assign({}, validazione, {
            dataModifica:
                validazione.dataModifica != null && validazione.dataModifica.isValid()
                    ? validazione.dataModifica.format(DATE_FORMAT)
                    : null,
            dataValidazione:
                validazione.dataValidazione != null && validazione.dataValidazione.isValid() ? validazione.dataValidazione.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dataModifica = res.body.dataModifica != null ? moment(res.body.dataModifica) : null;
        res.body.dataValidazione = res.body.dataValidazione != null ? moment(res.body.dataValidazione) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((validazione: IValidazione) => {
            validazione.dataModifica = validazione.dataModifica != null ? moment(validazione.dataModifica) : null;
            validazione.dataValidazione = validazione.dataValidazione != null ? moment(validazione.dataValidazione) : null;
        });
        return res;
    }
}
