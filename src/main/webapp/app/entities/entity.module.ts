import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TelefoniaServiziModule } from './servizi/servizi.module';
import { TelefoniaTelefonoModule } from './telefono/telefono.module';
import { TelefoniaOperatoreModule } from './operatore/operatore.module';
import { TelefoniaTelefonoServiziModule } from './telefono-servizi/telefono-servizi.module';
import { TelefoniaListaOperatoriModule } from './lista-operatori/lista-operatori.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        TelefoniaServiziModule,
        TelefoniaTelefonoModule,
        TelefoniaOperatoreModule,
        TelefoniaTelefonoServiziModule,
        TelefoniaListaOperatoriModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TelefoniaEntityModule {}
