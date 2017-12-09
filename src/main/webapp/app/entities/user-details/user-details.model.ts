import { BaseEntity, User } from './../../shared';

const enum GenderEnum {
    'MALE',
    'FEMALE'
}

export class UserDetails implements BaseEntity {
    constructor(
        public id?: number,
        public gender?: GenderEnum,
        public birthDate?: any,
        public user?: User,
    ) {
    }
}
