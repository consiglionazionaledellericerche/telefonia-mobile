import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { NgbDateCustomParserFormatter } from './util/datecustom-adapter';
import { TelefoniaSharedLibsModule, TelefoniaSharedCommonModule, HasAnyAuthorityDirective } from './';

@NgModule({
    imports: [TelefoniaSharedLibsModule, TelefoniaSharedCommonModule],
    declarations: [HasAnyAuthorityDirective],
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
    exports: [TelefoniaSharedCommonModule, HasAnyAuthorityDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TelefoniaSharedModule {}
