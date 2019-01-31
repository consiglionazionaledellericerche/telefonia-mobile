import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IListaOperatori } from 'app/shared/model/lista-operatori.model';

type EntityResponseType = HttpResponse<IListaOperatori>;
type EntityArrayResponseType = HttpResponse<IListaOperatori[]>;

@Injectable({ providedIn: 'root' })
export class ListaOperatoriService {
    private resourceUrl = SERVER_API_URL + 'api/lista-operatoris';

    constructor(private http: HttpClient) {}

    create(listaOperatori: IListaOperatori): Observable<EntityResponseType> {
        return this.http.post<IListaOperatori>(this.resourceUrl, listaOperatori, { observe: 'response' });
    }

    update(listaOperatori: IListaOperatori): Observable<EntityResponseType> {
        return this.http.put<IListaOperatori>(this.resourceUrl, listaOperatori, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IListaOperatori>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IListaOperatori[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
