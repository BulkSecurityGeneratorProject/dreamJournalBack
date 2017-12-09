import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserDetailsComponent } from './user-details.component';
import { UserDetailsDetailComponent } from './user-details-detail.component';
import { UserDetailsPopupComponent } from './user-details-dialog.component';
import { UserDetailsDeletePopupComponent } from './user-details-delete-dialog.component';

export const userDetailsRoute: Routes = [
    {
        path: 'user-details',
        component: UserDetailsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dreamJournalApp.userDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'user-details/:id',
        component: UserDetailsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dreamJournalApp.userDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userDetailsPopupRoute: Routes = [
    {
        path: 'user-details-new',
        component: UserDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dreamJournalApp.userDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-details/:id/edit',
        component: UserDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dreamJournalApp.userDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-details/:id/delete',
        component: UserDetailsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dreamJournalApp.userDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
