import {Component, Input, OnInit} from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Location }                 from '@angular/common';
import 'rxjs/add/operator/switchMap';

import { ContractorService } from './contractor.service';
import {Contractor} from "./contractor";

@Component({
  selector: 'contractor-detail',
  templateUrl: "./contractor-detail.component.html",
  styleUrls: ["./contractor-detail.component.css"]
})

export class ContractorDetailComponent implements OnInit {
  @Input() contractor: Contractor;

  ngOnInit(): void {
    this.route.paramMap
      .switchMap((params: ParamMap) => this.contractorService.getContractor(+params.get('id')))
      .subscribe(hero => this.contractor = hero);
  }

  constructor(
    private contractorService: ContractorService,
    private route: ActivatedRoute,
    private location: Location
  ) {}

  goBack(): void {
    this.location.back();
  }
}
