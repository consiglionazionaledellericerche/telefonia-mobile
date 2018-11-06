/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TelefoniTestModule } from '../../../test.module';
import { OperatoreUpdateComponent } from 'app/entities/operatore/operatore-update.component';
import { OperatoreService } from 'app/entities/operatore/operatore.service';
import { Operatore } from 'app/shared/model/operatore.model';

describe('Component Tests', () => {
    describe('Operatore Management Update Component', () => {
        let comp: OperatoreUpdateComponent;
        let fixture: ComponentFixture<OperatoreUpdateComponent>;
        let service: OperatoreService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniTestModule],
                declarations: [OperatoreUpdateComponent]
            })
                .overrideTemplate(OperatoreUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OperatoreUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OperatoreService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Operatore(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.operatore = entity;
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
                    const entity = new Operatore();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.operatore = entity;
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
