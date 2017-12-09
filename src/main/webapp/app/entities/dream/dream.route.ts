import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { DreamComponent } from './dream.component';
import { DreamDetailComponent } from './dream-detail.component';
import { DreamPopupComponent } from './dream-dialog.component';
import { DreamDeletePopupComponent } from './dream-delete-dialog.component';

@Injectable()
export class DreamResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const dreamRoute: Routes = [
    {
        path: 'dream',
        component: DreamComponent,
        resolve: {
            'pagingParams': DreamResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dreamJournalApp.dream.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'dream/:id',
        component: DreamDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dreamJournalApp.dream.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const dreamPopupRoute: Routes = [
    {
        path: 'dream-new',
        component: DreamPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dreamJournalApp.dream.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'dream/:id/edit',
        component: DreamPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dreamJournalApp.dream.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'dream/:id/delete',
        component: DreamDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dreamJournalApp.dream.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
