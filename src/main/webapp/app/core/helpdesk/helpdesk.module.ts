import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TelefoniaSharedModule } from 'app/shared';
import { HelpDeskComponent, helpDeskRoute } from './';

const ENTITY_STATES = [...helpDeskRoute];

@NgModule({
    imports: [TelefoniaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [HelpDeskComponent],
    entryComponents: [HelpDeskComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ParcoautoHelpDeskModule {}
