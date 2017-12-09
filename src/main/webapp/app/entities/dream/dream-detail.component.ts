import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Dream } from './dream.model';
import { DreamService } from './dream.service';

@Component({
    selector: 'jhi-dream-detail',
    templateUrl: './dream-detail.component.html'
})
export class DreamDetailComponent implements OnInit, OnDestroy {

    dream: Dream;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dreamService: DreamService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDreams();
    }

    load(id) {
        this.dreamService.find(id).subscribe((dream) => {
            this.dream = dream;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDreams() {
        this.eventSubscriber = this.eventManager.subscribe(
            'dreamListModification',
            (response) => this.load(this.dream.id)
        );
    }
}
