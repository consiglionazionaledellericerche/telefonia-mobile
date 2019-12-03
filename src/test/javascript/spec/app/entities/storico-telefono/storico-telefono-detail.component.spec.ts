/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TelefoniaTestModule } from '../../../test.module';
import { StoricoTelefonoDetailComponent } from 'app/entities/storico-telefono/storico-telefono-detail.component';
import { StoricoTelefono } from 'app/shared/model/storico-telefono.model';

describe('Component Tests', () => {
    describe('StoricoTelefono Management Detail Component', () => {
        let comp: StoricoTelefonoDetailComponent;
        let fixture: ComponentFixture<StoricoTelefonoDetailComponent>;
        const route = ({ data: of({ storicoTelefono: new StoricoTelefono(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniaTestModule],
                declarations: [StoricoTelefonoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(StoricoTelefonoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StoricoTelefonoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.storicoTelefono).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
