/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TelefoniTestModule } from '../../../test.module';
import { TelefonoserviziDetailComponent } from 'app/entities/telefonoservizi/telefonoservizi-detail.component';
import { Telefonoservizi } from 'app/shared/model/telefonoservizi.model';

describe('Component Tests', () => {
    describe('Telefonoservizi Management Detail Component', () => {
        let comp: TelefonoserviziDetailComponent;
        let fixture: ComponentFixture<TelefonoserviziDetailComponent>;
        const route = ({ data: of({ telefonoservizi: new Telefonoservizi(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniTestModule],
                declarations: [TelefonoserviziDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(TelefonoserviziDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TelefonoserviziDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.telefonoservizi).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
