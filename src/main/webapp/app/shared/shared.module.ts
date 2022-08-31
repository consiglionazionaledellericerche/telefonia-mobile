import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { NgbDateCustomParserFormatter } from './util/datecustom-adapter';
import { TelefoniaSharedLibsModule, TelefoniaSharedCommonModule, HasAnyAuthorityDirective } from './';
import { NotHasAnyAuthorityDirective } from './auth/not-has-any-authority.directive';

@NgModule({
    imports: [TelefoniaSharedLibsModule, TelefoniaSharedCommonModule],
    declarations: [HasAnyAuthorityDirective, NotHasAnyAuthorityDirective],
    providers: [
        {
            provide: NgbDateAdapter,
            useClass: NgbDateMomentAdapter
        },
        {
            provide: NgbDateParserFormatter,
            useClass: NgbDateCustomParserFormatter
        }
    ],

    entryComponents: [],
    exports: [TelefoniaSharedCommonModule, HasAnyAuthorityDirective, NotHasAnyAuthorityDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TelefoniaSharedModule {}
