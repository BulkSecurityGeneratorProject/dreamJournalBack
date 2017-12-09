import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Dream } from './dream.model';
import { DreamPopupService } from './dream-popup.service';
import { DreamService } from './dream.service';

@Component({
    selector: 'jhi-dream-delete-dialog',
    templateUrl: './dream-delete-dialog.component.html'
})
export class DreamDeleteDialogComponent {

    dream: Dream;

    constructor(
        private dreamService: DreamService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.dreamService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'dreamListModification',
                content: 'Deleted an dream'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-dream-delete-popup',
    template: ''
})
export class DreamDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private dreamPopupService: DreamPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.dreamPopupService
                .open(DreamDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
