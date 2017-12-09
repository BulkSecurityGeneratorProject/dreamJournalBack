import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { UserDetails } from './user-details.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class UserDetailsService {

    private resourceUrl = 'api/user-details';
    private resourceSearchUrl = 'api/_search/user-details';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(userDetails: UserDetails): Observable<UserDetails> {
        const copy = this.convert(userDetails);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(userDetails: UserDetails): Observable<UserDetails> {
        const copy = this.convert(userDetails);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<UserDetails> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.birthDate = this.dateUtils
            .convertDateTimeFromServer(entity.birthDate);
    }

    private convert(userDetails: UserDetails): UserDetails {
        const copy: UserDetails = Object.assign({}, userDetails);

        copy.birthDate = this.dateUtils.toDate(userDetails.birthDate);
        return copy;
    }
}
