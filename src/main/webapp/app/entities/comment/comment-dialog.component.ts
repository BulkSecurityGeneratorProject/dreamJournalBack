import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Comment } from './comment.model';
import { CommentPopupService } from './comment-popup.service';
import { CommentService } from './comment.service';
import { User, UserService } from '../../shared';
import { Dream, DreamService } from '../dream';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-comment-dialog',
    templateUrl: './comment-dialog.component.html'
})
export class CommentDialogComponent implements OnInit {

    comment: Comment;
    isSaving: boolean;

    users: User[];

    dreams: Dream[];

    comments: Comment[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private commentService: CommentService,
        private userService: UserService,
        private dreamService: DreamService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.dreamService.query()
            .subscribe((res: ResponseWrapper) => { this.dreams = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.commentService.query()
            .subscribe((res: ResponseWrapper) => { this.comments = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.comment.id !== undefined) {
            this.subscribeToSaveResponse(
                this.commentService.update(this.comment));
        } else {
            this.subscribeToSaveResponse(
                this.commentService.create(this.comment));
        }
    }

    private subscribeToSaveResponse(result: Observable<Comment>) {
        result.subscribe((res: Comment) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Comment) {
        this.eventManager.broadcast({ name: 'commentListModification', content: 'OK'});
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

    trackDreamById(index: number, item: Dream) {
        return item.id;
    }

    trackCommentById(index: number, item: Comment) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-comment-popup',
    template: ''
})
export class CommentPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private commentPopupService: CommentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.commentPopupService
                    .open(CommentDialogComponent as Component, params['id']);
            } else {
                this.commentPopupService
                    .open(CommentDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
