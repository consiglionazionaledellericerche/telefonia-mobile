import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { TelefonoServizi } from 'app/shared/model/telefono-servizi.model';
import { TelefonoServiziService } from './telefono-servizi.service';
import { TelefonoServiziComponent } from './telefono-servizi.component';
import { TelefonoServiziDetailComponent } from './telefono-servizi-detail.component';
import { TelefonoServiziUpdateComponent } from './telefono-servizi-update.component';
import { TelefonoServiziDeletePopupComponent } from './telefono-servizi-delete-dialog.component';
import { ITelefonoServizi } from 'app/shared/model/telefono-servizi.model';

@Injectable({ providedIn: 'root' })
export class TelefonoServiziResolve implements Resolve<ITelefonoServizi> {
    constructor(private service: TelefonoServiziService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((telefonoServizi: HttpResponse<TelefonoServizi>) => telefonoServizi.body));
        }
        return of(new TelefonoServizi());
    }
}

export const telefonoServiziRoute: Routes = [
    {
        path: 'telefono-servizi',
        component: TelefonoServiziComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'telefoniaApp.telefonoServizi.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'telefono-servizi/:id/view',
        component: TelefonoServiziDetailComponent,
        resolve: {
            telefonoServizi: TelefonoServiziResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.telefonoServizi.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'telefono-servizi/new',
        component: TelefonoServiziUpdateComponent,
        resolve: {
            telefonoServizi: TelefonoServiziResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.telefonoServizi.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'telefono-servizi/:id/edit',
        component: TelefonoServiziUpdateComponent,
        resolve: {
            telefonoServizi: TelefonoServiziResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.telefonoServizi.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const telefonoServiziPopupRoute: Routes = [
    {
        path: 'telefono-servizi/:id/delete',
        component: TelefonoServiziDeletePopupComponent,
        resolve: {
            telefonoServizi: TelefonoServiziResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniaApp.telefonoServizi.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
