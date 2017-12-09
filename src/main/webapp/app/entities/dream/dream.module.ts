import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DreamJournalSharedModule } from '../../shared';
import { DreamJournalAdminModule } from '../../admin/admin.module';
import {
    DreamService,
    DreamPopupService,
    DreamComponent,
    DreamDetailComponent,
    DreamDialogComponent,
    DreamPopupComponent,
    DreamDeletePopupComponent,
    DreamDeleteDialogComponent,
    dreamRoute,
    dreamPopupRoute,
    DreamResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...dreamRoute,
    ...dreamPopupRoute,
];

@NgModule({
    imports: [
        DreamJournalSharedModule,
        DreamJournalAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        DreamComponent,
        DreamDetailComponent,
        DreamDialogComponent,
        DreamDeleteDialogComponent,
        DreamPopupComponent,
        DreamDeletePopupComponent,
    ],
    entryComponents: [
        DreamComponent,
        DreamDialogComponent,
        DreamPopupComponent,
        DreamDeleteDialogComponent,
        DreamDeletePopupComponent,
    ],
    providers: [
        DreamService,
        DreamPopupService,
        DreamResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DreamJournalDreamModule {}
