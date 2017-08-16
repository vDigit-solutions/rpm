import { Component, OnInit } from '@angular/core';

import { Contractor } from './contractor';
import { ContractorService } from './contractor.service';

@Component({
  selector: 'my-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {
  contractors: Contractor[] = [];

  constructor(private contractorService: ContractorService) { }

  ngOnInit(): void {
    this.contractorService.getContractors()
      // .then(contractors => this.contractors = contractors.slice(1, 4));
      .then(contractors => this.contractors = contractors);
  }
}