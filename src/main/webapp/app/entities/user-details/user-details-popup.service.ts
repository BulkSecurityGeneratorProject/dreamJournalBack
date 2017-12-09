import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { UserDetails } from './user-details.model';
import { UserDetailsService } from './user-details.service';

@Injectable()
export class UserDetailsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private userDetailsService: UserDetailsService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.userDetailsService.find(id).subscribe((userDetails) => {
                    userDetails.birthDate = this.datePipe
                        .transform(userDetails.birthDate, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.userDetailsModalRef(component, userDetails);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.userDetailsModalRef(component, new UserDetails());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    userDetailsModalRef(component: Component, userDetails: UserDetails): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.userDetails = userDetails;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
