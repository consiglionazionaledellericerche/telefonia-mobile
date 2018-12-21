import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TelefoniaSharedModule } from 'app/shared';
import {
    OperatoreComponent,
    OperatoreDetailComponent,
    OperatoreUpdateComponent,
    OperatoreDeletePopupComponent,
    OperatoreDeleteDialogComponent,
    operatoreRoute,
    operatorePopupRoute
} from './';

const ENTITY_STATES = [...operatoreRoute, ...operatorePopupRoute];

@NgModule({
    imports: [TelefoniaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OperatoreComponent,
        OperatoreDetailComponent,
        OperatoreUpdateComponent,
        OperatoreDeleteDialogComponent,
        OperatoreDeletePopupComponent
    ],
    entryComponents: [OperatoreComponent, OperatoreUpdateComponent, OperatoreDeleteDialogComponent, OperatoreDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TelefoniaOperatoreModule {}
