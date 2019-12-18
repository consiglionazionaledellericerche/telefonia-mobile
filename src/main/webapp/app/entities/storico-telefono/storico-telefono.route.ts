import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { StoricoTelefono } from 'app/shared/model/storico-telefono.model';
import { StoricoTelefonoService } from './storico-telefono.service';
import { StoricoTelefonoComponent } from './storico-telefono.component';
import { StoricoTelefonoVistaComponent } from './storico-telefono-vista.component';
import { StoricoTelefonoDetailComponent } from './storico-telefono-detail.component';
import { StoricoTelefonoUpdateComponent } from './storico-telefono-update.component';
import { StoricoTelefonoDeletePopupComponent } from './storico-telefono-delete-dialog.component';
import { IStoricoTelefono } from 'app/shared/model/storico-telefono.model';

@Injectable({ providedIn: 'root' })
export class StoricoTelefonoResolve implements Resolve<IStoricoTelefono> {
    constructor(private service: StoricoTelefonoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((storicoTelefono: HttpResponse<StoricoTelefono>) => storicoTelefono.body));
        }
        return of(new StoricoTelefono());
    }
}

export const storicoTelefonoRoute: Routes = [
    {
        path: 'storico-telefono',
        component: StoricoTelefonoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'telefoniaApp.storicoTelefono.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'storico-telefono/vista',
        component: StoricoTelefonoVistaComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'telefoniaApp.storicoTelefono.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'storico-telefono/:id/view',
        component: StoricoTelefonoDetailComponent,
        resolve: {
            storicoTelefono: StoricoTelefonoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.storicoTelefono.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'storico-telefono/new',
        component: StoricoTelefonoUpdateComponent,
        resolve: {
            storicoTelefono: StoricoTelefonoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.storicoTelefono.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'storico-telefono/:id/edit',
        component: StoricoTelefonoUpdateComponent,
        resolve: {
            storicoTelefono: StoricoTelefonoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.storicoTelefono.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const storicoTelefonoPopupRoute: Routes = [
    {
        path: 'storico-telefono/:id/delete',
        component: StoricoTelefonoDeletePopupComponent,
        resolve: {
            storicoTelefono: StoricoTelefonoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.storicoTelefono.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
