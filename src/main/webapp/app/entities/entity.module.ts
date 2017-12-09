import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { DreamJournalCommentModule } from './comment/comment.module';
import { DreamJournalDreamModule } from './dream/dream.module';
import { DreamJournalUserDetailsModule } from './user-details/user-details.module';
import { DreamJournalTagModule } from './tag/tag.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        DreamJournalCommentModule,
        DreamJournalDreamModule,
        DreamJournalUserDetailsModule,
        DreamJournalTagModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DreamJournalEntityModule {}
