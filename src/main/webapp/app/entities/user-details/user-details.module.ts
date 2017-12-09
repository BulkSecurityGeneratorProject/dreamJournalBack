import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DreamJournalSharedModule } from '../../shared';
import { DreamJournalAdminModule } from '../../admin/admin.module';
import {
    UserDetailsService,
    UserDetailsPopupService,
    UserDetailsComponent,
    UserDetailsDetailComponent,
    UserDetailsDialogComponent,
    UserDetailsPopupComponent,
    UserDetailsDeletePopupComponent,
    UserDetailsDeleteDialogComponent,
    userDetailsRoute,
    userDetailsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...userDetailsRoute,
    ...userDetailsPopupRoute,
];

@NgModule({
    imports: [
        DreamJournalSharedModule,
        DreamJournalAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        UserDetailsComponent,
        UserDetailsDetailComponent,
        UserDetailsDialogComponent,
        UserDetailsDeleteDialogComponent,
        UserDetailsPopupComponent,
        UserDetailsDeletePopupComponent,
    ],
    entryComponents: [
        UserDetailsComponent,
        UserDetailsDialogComponent,
        UserDetailsPopupComponent,
        UserDetailsDeleteDialogComponent,
        UserDetailsDeletePopupComponent,
    ],
    providers: [
        UserDetailsService,
        UserDetailsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DreamJournalUserDetailsModule {}
