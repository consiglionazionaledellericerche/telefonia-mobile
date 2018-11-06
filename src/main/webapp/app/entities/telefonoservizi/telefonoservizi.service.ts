import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITelefonoservizi } from 'app/shared/model/telefonoservizi.model';

type EntityResponseType = HttpResponse<ITelefonoservizi>;
type EntityArrayResponseType = HttpResponse<ITelefonoservizi[]>;

@Injectable({ providedIn: 'root' })
export class TelefonoserviziService {
    private resourceUrl = SERVER_API_URL + 'api/telefonoservizis';

    constructor(private http: HttpClient) {}

    create(telefonoservizi: ITelefonoservizi): Observable<EntityResponseType> {
        return this.http.post<ITelefonoservizi>(this.resourceUrl, telefonoservizi, { observe: 'response' });
    }

    update(telefonoservizi: ITelefonoservizi): Observable<EntityResponseType> {
        return this.http.put<ITelefonoservizi>(this.resourceUrl, telefonoservizi, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ITelefonoservizi>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITelefonoservizi[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
