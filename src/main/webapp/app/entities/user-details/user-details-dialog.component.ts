import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { UserDetails } from './user-details.model';
import { UserDetailsPopupService } from './user-details-popup.service';
import { UserDetailsService } from './user-details.service';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-user-details-dialog',
    templateUrl: './user-details-dialog.component.html'
})
export class UserDetailsDialogComponent implements OnInit {

    userDetails: UserDetails;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private userDetailsService: UserDetailsService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.userDetails.id !== undefined) {
            this.subscribeToSaveResponse(
                this.userDetailsService.update(this.userDetails));
        } else {
            this.subscribeToSaveResponse(
                this.userDetailsService.create(this.userDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<UserDetails>) {
        result.subscribe((res: UserDetails) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: UserDetails) {
        this.eventManager.broadcast({ name: 'userDetailsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-user-details-popup',
    template: ''
})
export class UserDetailsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userDetailsPopupService: UserDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.userDetailsPopupService
                    .open(UserDetailsDialogComponent as Component, params['id']);
            } else {
                this.userDetailsPopupService
                    .open(UserDetailsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
