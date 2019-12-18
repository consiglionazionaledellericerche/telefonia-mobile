import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TelefoniaSharedModule } from 'app/shared';
import {
    StoricoTelefonoComponent,
    StoricoTelefonoVistaComponent,
    StoricoTelefonoDetailComponent,
    StoricoTelefonoUpdateComponent,
    StoricoTelefonoDeletePopupComponent,
    StoricoTelefonoDeleteDialogComponent,
    storicoTelefonoRoute,
    storicoTelefonoPopupRoute
} from './';

const ENTITY_STATES = [...storicoTelefonoRoute, ...storicoTelefonoPopupRoute];

@NgModule({
    imports: [TelefoniaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        StoricoTelefonoComponent,
        StoricoTelefonoVistaComponent,
        StoricoTelefonoDetailComponent,
        StoricoTelefonoUpdateComponent,
        StoricoTelefonoDeleteDialogComponent,
        StoricoTelefonoDeletePopupComponent
    ],
    entryComponents: [
        StoricoTelefonoComponent,
        StoricoTelefonoVistaComponent,
        StoricoTelefonoUpdateComponent,
        StoricoTelefonoDeleteDialogComponent,
        StoricoTelefonoDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TelefoniaStoricoTelefonoModule {}
