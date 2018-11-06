import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IServizi } from 'app/shared/model/servizi.model';

type EntityResponseType = HttpResponse<IServizi>;
type EntityArrayResponseType = HttpResponse<IServizi[]>;

@Injectable({ providedIn: 'root' })
export class ServiziService {
    private resourceUrl = SERVER_API_URL + 'api/servizis';

    constructor(private http: HttpClient) {}

    create(servizi: IServizi): Observable<EntityResponseType> {
        return this.http.post<IServizi>(this.resourceUrl, servizi, { observe: 'response' });
    }

    update(servizi: IServizi): Observable<EntityResponseType> {
        return this.http.put<IServizi>(this.resourceUrl, servizi, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IServizi>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IServizi[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
