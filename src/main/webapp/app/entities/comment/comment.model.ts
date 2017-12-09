import { BaseEntity, User } from './../../shared';

export class Comment implements BaseEntity {
    constructor(
        public id?: number,
        public content?: string,
        public createDate?: any,
        public isReply?: boolean,
        public user?: User,
        public dream?: BaseEntity,
        public childs?: BaseEntity,
        public parents?: BaseEntity[],
    ) {
        this.isReply = false;
    }
}
