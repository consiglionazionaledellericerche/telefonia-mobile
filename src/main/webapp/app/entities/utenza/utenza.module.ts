import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TelefoniSharedModule } from 'app/shared';
import {
    UtenzaComponent,
    UtenzaDetailComponent,
    UtenzaUpdateComponent,
    UtenzaDeletePopupComponent,
    UtenzaDeleteDialogComponent,
    utenzaRoute,
    utenzaPopupRoute
} from './';

const ENTITY_STATES = [...utenzaRoute, ...utenzaPopupRoute];

@NgModule({
    imports: [TelefoniSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [UtenzaComponent, UtenzaDetailComponent, UtenzaUpdateComponent, UtenzaDeleteDialogComponent, UtenzaDeletePopupComponent],
    entryComponents: [UtenzaComponent, UtenzaUpdateComponent, UtenzaDeleteDialogComponent, UtenzaDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TelefoniUtenzaModule {}
