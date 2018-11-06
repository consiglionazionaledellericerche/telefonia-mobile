/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TelefoniTestModule } from '../../../test.module';
import { TelefonoDetailComponent } from 'app/entities/telefono/telefono-detail.component';
import { Telefono } from 'app/shared/model/telefono.model';

describe('Component Tests', () => {
    describe('Telefono Management Detail Component', () => {
        let comp: TelefonoDetailComponent;
        let fixture: ComponentFixture<TelefonoDetailComponent>;
        const route = ({ data: of({ telefono: new Telefono(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniTestModule],
                declarations: [TelefonoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(TelefonoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TelefonoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.telefono).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
