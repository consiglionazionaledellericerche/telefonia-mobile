/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TelefoniaTestModule } from '../../../test.module';
import { TelefonoServiziDetailComponent } from 'app/entities/telefono-servizi/telefono-servizi-detail.component';
import { TelefonoServizi } from 'app/shared/model/telefono-servizi.model';

describe('Component Tests', () => {
    describe('TelefonoServizi Management Detail Component', () => {
        let comp: TelefonoServiziDetailComponent;
        let fixture: ComponentFixture<TelefonoServiziDetailComponent>;
        const route = ({ data: of({ telefonoServizi: new TelefonoServizi(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniaTestModule],
                declarations: [TelefonoServiziDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(TelefonoServiziDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TelefonoServiziDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.telefonoServizi).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
