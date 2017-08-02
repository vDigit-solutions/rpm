import {Component, Input, OnInit} from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Location }                 from '@angular/common';
import 'rxjs/add/operator/switchMap';

import { ContractorService } from './contractor.service';
import {Hero} from "./contractor";

@Component({
  selector: 'hero-detail',
  templateUrl: "./hero-detail.component.html",
  styleUrls: ["./hero-detail.component.css"]
})

export class HeroDetailComponent implements OnInit {
  @Input() hero: Hero;

  ngOnInit(): void {
    this.route.paramMap
      .switchMap((params: ParamMap) => this.heroService.getHero(+params.get('id')))
      .subscribe(hero => this.hero = hero);
  }

  constructor(
    private heroService: ContractorService,
    private route: ActivatedRoute,
    private location: Location
  ) {}

  goBack(): void {
    this.location.back();
  }


}
