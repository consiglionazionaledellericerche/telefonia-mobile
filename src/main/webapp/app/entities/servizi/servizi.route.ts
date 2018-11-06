import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Servizi } from 'app/shared/model/servizi.model';
import { ServiziService } from './servizi.service';
import { ServiziComponent } from './servizi.component';
import { ServiziDetailComponent } from './servizi-detail.component';
import { ServiziUpdateComponent } from './servizi-update.component';
import { ServiziDeletePopupComponent } from './servizi-delete-dialog.component';
import { IServizi } from 'app/shared/model/servizi.model';

@Injectable({ providedIn: 'root' })
export class ServiziResolve implements Resolve<IServizi> {
    constructor(private service: ServiziService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((servizi: HttpResponse<Servizi>) => servizi.body));
        }
        return of(new Servizi());
    }
}

export const serviziRoute: Routes = [
    {
        path: 'servizi',
        component: ServiziComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'telefoniApp.servizi.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'servizi/:id/view',
        component: ServiziDetailComponent,
        resolve: {
            servizi: ServiziResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniApp.servizi.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'servizi/new',
        component: ServiziUpdateComponent,
        resolve: {
            servizi: ServiziResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniApp.servizi.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'servizi/:id/edit',
        component: ServiziUpdateComponent,
        resolve: {
            servizi: ServiziResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniApp.servizi.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const serviziPopupRoute: Routes = [
    {
        path: 'servizi/:id/delete',
        component: ServiziDeletePopupComponent,
        resolve: {
            servizi: ServiziResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'telefoniApp.servizi.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
