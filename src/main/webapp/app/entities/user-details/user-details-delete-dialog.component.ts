import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { UserDetails } from './user-details.model';
import { UserDetailsPopupService } from './user-details-popup.service';
import { UserDetailsService } from './user-details.service';

@Component({
    selector: 'jhi-user-details-delete-dialog',
    templateUrl: './user-details-delete-dialog.component.html'
})
export class UserDetailsDeleteDialogComponent {

    userDetails: UserDetails;

    constructor(
        private userDetailsService: UserDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userDetailsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'userDetailsListModification',
                content: 'Deleted an userDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-user-details-delete-popup',
    template: ''
})
export class UserDetailsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userDetailsPopupService: UserDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.userDetailsPopupService
                .open(UserDetailsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
