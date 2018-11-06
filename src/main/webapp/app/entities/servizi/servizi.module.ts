import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TelefoniSharedModule } from 'app/shared';
import {
    ServiziComponent,
    ServiziDetailComponent,
    ServiziUpdateComponent,
    ServiziDeletePopupComponent,
    ServiziDeleteDialogComponent,
    serviziRoute,
    serviziPopupRoute
} from './';

const ENTITY_STATES = [...serviziRoute, ...serviziPopupRoute];

@NgModule({
    imports: [TelefoniSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ServiziComponent,
        ServiziDetailComponent,
        ServiziUpdateComponent,
        ServiziDeleteDialogComponent,
        ServiziDeletePopupComponent
    ],
    entryComponents: [ServiziComponent, ServiziUpdateComponent, ServiziDeleteDialogComponent, ServiziDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TelefoniServiziModule {}
