/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { DreamJournalTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { DreamDetailComponent } from '../../../../../../main/webapp/app/entities/dream/dream-detail.component';
import { DreamService } from '../../../../../../main/webapp/app/entities/dream/dream.service';
import { Dream } from '../../../../../../main/webapp/app/entities/dream/dream.model';

describe('Component Tests', () => {

    describe('Dream Management Detail Component', () => {
        let comp: DreamDetailComponent;
        let fixture: ComponentFixture<DreamDetailComponent>;
        let service: DreamService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DreamJournalTestModule],
                declarations: [DreamDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    DreamService,
                    JhiEventManager
                ]
            }).overrideTemplate(DreamDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DreamDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DreamService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Dream(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.dream).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
