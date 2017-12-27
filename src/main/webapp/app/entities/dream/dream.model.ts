import { BaseEntity, User } from './../../shared';

export class Dream implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public createDate?: any,
        public description?: string,
        public isLucid?: boolean,
        public nightIndex?: number,
        public visibility?: boolean,
        public lenght?: number,
        public score?: number,
        public isAdult?: boolean,
        public user?: User,
        public tags?: BaseEntity[],
        public comments?: BaseEntity[],
    ) {
        this.isLucid = false;
        this.visibility = false;
        this.isAdult = false;
    }
}
