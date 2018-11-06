import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

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
        return this.http.post<IOperatore>(this.resourceUrl, operatore, { observe: 'response' });
    }

    update(operatore: IOperatore): Observable<EntityResponseType> {
        return this.http.put<IOperatore>(this.resourceUrl, operatore, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IOperatore>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOperatore[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
