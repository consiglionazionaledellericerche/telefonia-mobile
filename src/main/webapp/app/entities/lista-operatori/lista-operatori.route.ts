import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ListaOperatori } from 'app/shared/model/lista-operatori.model';
import { ListaOperatoriService } from './lista-operatori.service';
import { ListaOperatoriComponent } from './lista-operatori.component';
import { ListaOperatoriDetailComponent } from './lista-operatori-detail.component';
import { ListaOperatoriUpdateComponent } from './lista-operatori-update.component';
import { ListaOperatoriDeletePopupComponent } from './lista-operatori-delete-dialog.component';
import { IListaOperatori } from 'app/shared/model/lista-operatori.model';

@Injectable({ providedIn: 'root' })
export class ListaOperatoriResolve implements Resolve<IListaOperatori> {
    constructor(private service: ListaOperatoriService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((listaOperatori: HttpResponse<ListaOperatori>) => listaOperatori.body));
        }
        return of(new ListaOperatori());
    }
}

export const listaOperatoriRoute: Routes = [
    {
        path: 'lista-operatori',
        component: ListaOperatoriComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'telefoniaApp.listaOperatori.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lista-operatori/:id/view',
        component: ListaOperatoriDetailComponent,
        resolve: {
            listaOperatori: ListaOperatoriResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.listaOperatori.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lista-operatori/new',
        component: ListaOperatoriUpdateComponent,
        resolve: {
            listaOperatori: ListaOperatoriResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.listaOperatori.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lista-operatori/:id/edit',
        component: ListaOperatoriUpdateComponent,
        resolve: {
            listaOperatori: ListaOperatoriResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.listaOperatori.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const listaOperatoriPopupRoute: Routes = [
    {
        path: 'lista-operatori/:id/delete',
        component: ListaOperatoriDeletePopupComponent,
        resolve: {
            listaOperatori: ListaOperatoriResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.listaOperatori.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
