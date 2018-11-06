import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TelefoniSharedModule } from 'app/shared';
import {
    TelefonoserviziComponent,
    TelefonoserviziDetailComponent,
    TelefonoserviziUpdateComponent,
    TelefonoserviziDeletePopupComponent,
    TelefonoserviziDeleteDialogComponent,
    telefonoserviziRoute,
    telefonoserviziPopupRoute
} from './';

const ENTITY_STATES = [...telefonoserviziRoute, ...telefonoserviziPopupRoute];

@NgModule({
    imports: [TelefoniSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TelefonoserviziComponent,
        TelefonoserviziDetailComponent,
        TelefonoserviziUpdateComponent,
        TelefonoserviziDeleteDialogComponent,
        TelefonoserviziDeletePopupComponent
    ],
    entryComponents: [
        TelefonoserviziComponent,
        TelefonoserviziUpdateComponent,
        TelefonoserviziDeleteDialogComponent,
        TelefonoserviziDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TelefoniTelefonoserviziModule {}
