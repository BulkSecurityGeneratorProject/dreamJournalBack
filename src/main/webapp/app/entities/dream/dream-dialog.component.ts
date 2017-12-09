import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Dream } from './dream.model';
import { DreamPopupService } from './dream-popup.service';
import { DreamService } from './dream.service';
import { User, UserService } from '../../shared';
import { Tag, TagService } from '../tag';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-dream-dialog',
    templateUrl: './dream-dialog.component.html'
})
export class DreamDialogComponent implements OnInit {

    dream: Dream;
    isSaving: boolean;

    users: User[];

    tags: Tag[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private dreamService: DreamService,
        private userService: UserService,
        private tagService: TagService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.tagService.query()
            .subscribe((res: ResponseWrapper) => { this.tags = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.dream.id !== undefined) {
            this.subscribeToSaveResponse(
                this.dreamService.update(this.dream));
        } else {
            this.subscribeToSaveResponse(
                this.dreamService.create(this.dream));
        }
    }

    private subscribeToSaveResponse(result: Observable<Dream>) {
        result.subscribe((res: Dream) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Dream) {
        this.eventManager.broadcast({ name: 'dreamListModification', content: 'OK'});
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

    trackTagById(index: number, item: Tag) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-dream-popup',
    template: ''
})
export class DreamPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private dreamPopupService: DreamPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.dreamPopupService
                    .open(DreamDialogComponent as Component, params['id']);
            } else {
                this.dreamPopupService
                    .open(DreamDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
