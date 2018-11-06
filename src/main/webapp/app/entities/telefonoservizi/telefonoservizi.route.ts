import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Telefonoservizi } from 'app/shared/model/telefonoservizi.model';
import { TelefonoserviziService } from './telefonoservizi.service';
import { TelefonoserviziComponent } from './telefonoservizi.component';
import { TelefonoserviziDetailComponent } from './telefonoservizi-detail.component';
import { TelefonoserviziUpdateComponent } from './telefonoservizi-update.component';
import { TelefonoserviziDeletePopupComponent } from './telefonoservizi-delete-dialog.component';
import { ITelefonoservizi } from 'app/shared/model/telefonoservizi.model';

@Injectable({ providedIn: 'root' })
export class TelefonoserviziResolve implements Resolve<ITelefonoservizi> {
    constructor(private service: TelefonoserviziService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((telefonoservizi: HttpResponse<Telefonoservizi>) => telefonoservizi.body));
        }
        return of(new Telefonoservizi());
    }
}

export const telefonoserviziRoute: Routes = [
    {
        path: 'telefonoservizi',
        component: TelefonoserviziComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'telefoniApp.telefonoservizi.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'telefonoservizi/:id/view',
        component: TelefonoserviziDetailComponent,
        resolve: {
            telefonoservizi: TelefonoserviziResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniApp.telefonoservizi.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'telefonoservizi/new',
        component: TelefonoserviziUpdateComponent,
        resolve: {
            telefonoservizi: TelefonoserviziResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniApp.telefonoservizi.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'telefonoservizi/:id/edit',
        component: TelefonoserviziUpdateComponent,
        resolve: {
            telefonoservizi: TelefonoserviziResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniApp.telefonoservizi.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const telefonoserviziPopupRoute: Routes = [
    {
        path: 'telefonoservizi/:id/delete',
        component: TelefonoserviziDeletePopupComponent,
        resolve: {
            telefonoservizi: TelefonoserviziResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniApp.telefonoservizi.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
