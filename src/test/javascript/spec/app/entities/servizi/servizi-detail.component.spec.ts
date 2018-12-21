/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TelefoniaTestModule } from '../../../test.module';
import { ServiziDetailComponent } from 'app/entities/servizi/servizi-detail.component';
import { Servizi } from 'app/shared/model/servizi.model';

describe('Component Tests', () => {
    describe('Servizi Management Detail Component', () => {
        let comp: ServiziDetailComponent;
        let fixture: ComponentFixture<ServiziDetailComponent>;
        const route = ({ data: of({ servizi: new Servizi(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniaTestModule],
                declarations: [ServiziDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ServiziDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ServiziDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.servizi).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
