import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TelefoniaSharedModule } from 'app/shared';
import {
    TelefonoServiziComponent,
    TelefonoServiziDetailComponent,
    TelefonoServiziUpdateComponent,
    TelefonoServiziDeletePopupComponent,
    TelefonoServiziDeleteDialogComponent,
    telefonoServiziRoute,
    telefonoServiziPopupRoute
} from './';

const ENTITY_STATES = [...telefonoServiziRoute, ...telefonoServiziPopupRoute];

@NgModule({
    imports: [TelefoniaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TelefonoServiziComponent,
        TelefonoServiziDetailComponent,
        TelefonoServiziUpdateComponent,
        TelefonoServiziDeleteDialogComponent,
        TelefonoServiziDeletePopupComponent
    ],
    entryComponents: [
        TelefonoServiziComponent,
        TelefonoServiziUpdateComponent,
        TelefonoServiziDeleteDialogComponent,
        TelefonoServiziDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TelefoniaTelefonoServiziModule {}
