/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TelefoniaTestModule } from '../../../test.module';
import { OperatoreDetailComponent } from 'app/entities/operatore/operatore-detail.component';
import { Operatore } from 'app/shared/model/operatore.model';

describe('Component Tests', () => {
    describe('Operatore Management Detail Component', () => {
        let comp: OperatoreDetailComponent;
        let fixture: ComponentFixture<OperatoreDetailComponent>;
        const route = ({ data: of({ operatore: new Operatore(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniaTestModule],
                declarations: [OperatoreDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OperatoreDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OperatoreDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.operatore).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
