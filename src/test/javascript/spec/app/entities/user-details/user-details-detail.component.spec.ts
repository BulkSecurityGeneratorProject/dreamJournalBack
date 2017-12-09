/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { DreamJournalTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { UserDetailsDetailComponent } from '../../../../../../main/webapp/app/entities/user-details/user-details-detail.component';
import { UserDetailsService } from '../../../../../../main/webapp/app/entities/user-details/user-details.service';
import { UserDetails } from '../../../../../../main/webapp/app/entities/user-details/user-details.model';

describe('Component Tests', () => {

    describe('UserDetails Management Detail Component', () => {
        let comp: UserDetailsDetailComponent;
        let fixture: ComponentFixture<UserDetailsDetailComponent>;
        let service: UserDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DreamJournalTestModule],
                declarations: [UserDetailsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    UserDetailsService,
                    JhiEventManager
                ]
            }).overrideTemplate(UserDetailsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserDetailsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new UserDetails(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.userDetails).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
