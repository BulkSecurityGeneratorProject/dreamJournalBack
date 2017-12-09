import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DreamJournalSharedModule } from '../../shared';
import { DreamJournalAdminModule } from '../../admin/admin.module';
import {
    CommentService,
    CommentPopupService,
    CommentComponent,
    CommentDetailComponent,
    CommentDialogComponent,
    CommentPopupComponent,
    CommentDeletePopupComponent,
    CommentDeleteDialogComponent,
    commentRoute,
    commentPopupRoute,
    CommentResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...commentRoute,
    ...commentPopupRoute,
];

@NgModule({
    imports: [
        DreamJournalSharedModule,
        DreamJournalAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CommentComponent,
        CommentDetailComponent,
        CommentDialogComponent,
        CommentDeleteDialogComponent,
        CommentPopupComponent,
        CommentDeletePopupComponent,
    ],
    entryComponents: [
        CommentComponent,
        CommentDialogComponent,
        CommentPopupComponent,
        CommentDeleteDialogComponent,
        CommentDeletePopupComponent,
    ],
    providers: [
        CommentService,
        CommentPopupService,
        CommentResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DreamJournalCommentModule {}
