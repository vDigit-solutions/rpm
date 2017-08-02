import { Component, OnInit } from '@angular/core';

import { Contractor } from './contractor';
import { ContractorService } from './contractor.service';

@Component({
  selector: 'my-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {
  heroes: Contractor[] = [];

  constructor(private heroService: ContractorService) { }

  ngOnInit(): void {
    this.heroService.getHeroes()
      .then(heroes => this.heroes = heroes.slice(1, 5));
  }
}
