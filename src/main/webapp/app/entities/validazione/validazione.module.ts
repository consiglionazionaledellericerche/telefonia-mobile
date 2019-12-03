import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TelefoniaSharedModule } from 'app/shared';
import {
    ValidazioneComponent,
    ValidazioneDetailComponent,
    ValidazioneUpdateComponent,
    ValidazioneDeletePopupComponent,
    ValidazioneDeleteDialogComponent,
    validazioneRoute,
    validazionePopupRoute
} from './';

const ENTITY_STATES = [...validazioneRoute, ...validazionePopupRoute];

@NgModule({
    imports: [TelefoniaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ValidazioneComponent,
        ValidazioneDetailComponent,
        ValidazioneUpdateComponent,
        ValidazioneDeleteDialogComponent,
        ValidazioneDeletePopupComponent
    ],
    entryComponents: [ValidazioneComponent, ValidazioneUpdateComponent, ValidazioneDeleteDialogComponent, ValidazioneDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TelefoniaValidazioneModule {}
