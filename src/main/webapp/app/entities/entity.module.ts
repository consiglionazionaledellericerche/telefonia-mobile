import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TelefoniIstitutoModule } from './istituto/istituto.module';
import { TelefoniServiziModule } from './servizi/servizi.module';
import { TelefoniUtenzaModule } from './utenza/utenza.module';
import { TelefoniTelefonoModule } from './telefono/telefono.module';
import { TelefoniOperatoreModule } from './operatore/operatore.module';
import { TelefoniTelefonoserviziModule } from './telefonoservizi/telefonoservizi.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        TelefoniIstitutoModule,
        TelefoniServiziModule,
        TelefoniUtenzaModule,
        TelefoniTelefonoModule,
        TelefoniOperatoreModule,
        TelefoniTelefonoserviziModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TelefoniEntityModule {}
