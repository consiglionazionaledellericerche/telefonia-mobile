import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TelefoniaSharedModule } from 'app/shared';
import {
    ListaOperatoriComponent,
    ListaOperatoriDetailComponent,
    ListaOperatoriUpdateComponent,
    ListaOperatoriDeletePopupComponent,
    ListaOperatoriDeleteDialogComponent,
    listaOperatoriRoute,
    listaOperatoriPopupRoute
} from './';

const ENTITY_STATES = [...listaOperatoriRoute, ...listaOperatoriPopupRoute];

@NgModule({
    imports: [TelefoniaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ListaOperatoriComponent,
        ListaOperatoriDetailComponent,
        ListaOperatoriUpdateComponent,
        ListaOperatoriDeleteDialogComponent,
        ListaOperatoriDeletePopupComponent
    ],
    entryComponents: [
        ListaOperatoriComponent,
        ListaOperatoriUpdateComponent,
        ListaOperatoriDeleteDialogComponent,
        ListaOperatoriDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TelefoniaListaOperatoriModule {}
