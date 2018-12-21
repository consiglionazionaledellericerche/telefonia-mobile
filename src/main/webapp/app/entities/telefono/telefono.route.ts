import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Telefono } from 'app/shared/model/telefono.model';
import { TelefonoService } from './telefono.service';
import { TelefonoComponent } from './telefono.component';
import { TelefonoDetailComponent } from './telefono-detail.component';
import { TelefonoUpdateComponent } from './telefono-update.component';
import { TelefonoDeletePopupComponent } from './telefono-delete-dialog.component';
import { ITelefono } from 'app/shared/model/telefono.model';

@Injectable({ providedIn: 'root' })
export class TelefonoResolve implements Resolve<ITelefono> {
    constructor(private service: TelefonoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((telefono: HttpResponse<Telefono>) => telefono.body));
        }
        return of(new Telefono());
    }
}

export const telefonoRoute: Routes = [
    {
        path: 'telefono',
        component: TelefonoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'telefoniaApp.telefono.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'telefono/:id/view',
        component: TelefonoDetailComponent,
        resolve: {
            telefono: TelefonoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.telefono.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'telefono/new',
        component: TelefonoUpdateComponent,
        resolve: {
            telefono: TelefonoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.telefono.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'telefono/:id/edit',
        component: TelefonoUpdateComponent,
        resolve: {
            telefono: TelefonoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.telefono.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const telefonoPopupRoute: Routes = [
    {
        path: 'telefono/:id/delete',
        component: TelefonoDeletePopupComponent,
        resolve: {
            telefono: TelefonoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.telefono.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
