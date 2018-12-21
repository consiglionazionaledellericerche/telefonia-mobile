import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

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
        return this.http.post<ITelefonoServizi>(this.resourceUrl, telefonoServizi, { observe: 'response' });
    }

    update(telefonoServizi: ITelefonoServizi): Observable<EntityResponseType> {
        return this.http.put<ITelefonoServizi>(this.resourceUrl, telefonoServizi, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ITelefonoServizi>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITelefonoServizi[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
