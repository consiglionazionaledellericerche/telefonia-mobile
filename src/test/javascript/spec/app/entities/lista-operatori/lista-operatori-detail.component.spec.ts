/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TelefoniaTestModule } from '../../../test.module';
import { ListaOperatoriDetailComponent } from 'app/entities/lista-operatori/lista-operatori-detail.component';
import { ListaOperatori } from 'app/shared/model/lista-operatori.model';

describe('Component Tests', () => {
    describe('ListaOperatori Management Detail Component', () => {
        let comp: ListaOperatoriDetailComponent;
        let fixture: ComponentFixture<ListaOperatoriDetailComponent>;
        const route = ({ data: of({ listaOperatori: new ListaOperatori(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniaTestModule],
                declarations: [ListaOperatoriDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ListaOperatoriDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ListaOperatoriDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.listaOperatori).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
