import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOperatore } from 'app/shared/model/operatore.model';

type EntityResponseType = HttpResponse<IOperatore>;
type EntityArrayResponseType = HttpResponse<IOperatore[]>;

@Injectable({ providedIn: 'root' })
export class OperatoreService {
    private resourceUrl = SERVER_API_URL + 'api/operatores';

    constructor(private http: HttpClient) {}

    create(operatore: IOperatore): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(operatore);
        return this.http
            .post<IOperatore>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(operatore: IOperatore): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(operatore);
        return this.http
            .put<IOperatore>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IOperatore>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOperatore[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(operatore: IOperatore): IOperatore {
        const copy: IOperatore = Object.assign({}, operatore, {
            data: operatore.data != null && operatore.data.isValid() ? operatore.data.toJSON() : null,
            dataFine: operatore.dataFine != null && operatore.dataFine.isValid() ? operatore.dataFine.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.data = res.body.data != null ? moment(res.body.data) : null;
        res.body.dataFine = res.body.dataFine != null ? moment(res.body.dataFine) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((operatore: IOperatore) => {
            operatore.data = operatore.data != null ? moment(operatore.data) : null;
            operatore.dataFine = operatore.dataFine != null ? moment(operatore.dataFine) : null;
        });
        return res;
    }
    findTelefono() {
        return this.http.get<any>(`${this.resourceUrl}/findTelefono`);
    }

    telefonoAnno(anno: number): Observable<HttpResponse<any>> {
        console.log('entriiiii');
        return this.http.get<any>(`${this.resourceUrl}/telefoniAnno/${anno}`, { observe: 'response' });
    }
}
