/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TelefoniaTestModule } from '../../../test.module';
import { ServiziUpdateComponent } from 'app/entities/servizi/servizi-update.component';
import { ServiziService } from 'app/entities/servizi/servizi.service';
import { Servizi } from 'app/shared/model/servizi.model';

describe('Component Tests', () => {
    describe('Servizi Management Update Component', () => {
        let comp: ServiziUpdateComponent;
        let fixture: ComponentFixture<ServiziUpdateComponent>;
        let service: ServiziService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniaTestModule],
                declarations: [ServiziUpdateComponent]
            })
                .overrideTemplate(ServiziUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ServiziUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ServiziService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Servizi(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.servizi = entity;
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
                    const entity = new Servizi();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.servizi = entity;
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
