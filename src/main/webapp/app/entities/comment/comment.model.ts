import { BaseEntity, User } from './../../shared';

export class Comment implements BaseEntity {
    constructor(
        public id?: number,
        public content?: string,
        public createDate?: any,
        public isReply?: boolean,
        public user?: User,
        public dream?: BaseEntity,
        public parent?: BaseEntity,
        public childs?: BaseEntity[],
    ) {
        this.isReply = false;
    }
}
