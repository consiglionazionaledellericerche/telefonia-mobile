import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TelefoniSharedModule } from 'app/shared';
import {
    TelefonoComponent,
    TelefonoDetailComponent,
    TelefonoUpdateComponent,
    TelefonoDeletePopupComponent,
    TelefonoDeleteDialogComponent,
    telefonoRoute,
    telefonoPopupRoute
} from './';

const ENTITY_STATES = [...telefonoRoute, ...telefonoPopupRoute];

@NgModule({
    imports: [TelefoniSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TelefonoComponent,
        TelefonoDetailComponent,
        TelefonoUpdateComponent,
        TelefonoDeleteDialogComponent,
        TelefonoDeletePopupComponent
    ],
    entryComponents: [TelefonoComponent, TelefonoUpdateComponent, TelefonoDeleteDialogComponent, TelefonoDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TelefoniTelefonoModule {}
