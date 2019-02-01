/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TelefoniaTestModule } from '../../../test.module';
import { ListaOperatoriUpdateComponent } from 'app/entities/lista-operatori/lista-operatori-update.component';
import { ListaOperatoriService } from 'app/entities/lista-operatori/lista-operatori.service';
import { ListaOperatori } from 'app/shared/model/lista-operatori.model';

describe('Component Tests', () => {
    describe('ListaOperatori Management Update Component', () => {
        let comp: ListaOperatoriUpdateComponent;
        let fixture: ComponentFixture<ListaOperatoriUpdateComponent>;
        let service: ListaOperatoriService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniaTestModule],
                declarations: [ListaOperatoriUpdateComponent]
            })
                .overrideTemplate(ListaOperatoriUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ListaOperatoriUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ListaOperatoriService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ListaOperatori(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.listaOperatori = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ListaOperatori();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.listaOperatori = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
