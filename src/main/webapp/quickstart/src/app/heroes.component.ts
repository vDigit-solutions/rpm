import {Component, OnInit} from '@angular/core';
import {Contractor} from "./contractor";
import { ContractorService } from './contractor.service';
import {Router} from "@angular/router";


@Component({
  selector: 'my-heroes',
  templateUrl: "./heroes.component.html",
  styleUrls: ["./heroes.component.css"]
})

export class HeroesComponent implements OnInit{

  selectedHero: Contractor;
  heroes: Contractor[];

  constructor(private heroService: ContractorService, private router: Router) {}

  ngOnInit(): void {
    this.getHeroes();
  }

  getHeroes(): void {
    this.heroService.getHeroes().then(heroes=> this.heroes = heroes);
  }

  onSelect(h:Contractor): void {
    this.selectedHero = h;
  }

  gotoDetail(): void {
    this.router.navigate(['/detail', this.selectedHero.id]);
  }
}


