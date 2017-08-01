import {Component, OnInit} from '@angular/core';
import {Hero} from "./hero";
import { HeroService } from './hero.service';
import {Router} from "@angular/router";


@Component({
  selector: 'my-heroes',
  templateUrl: "./heroes.component.html",
  styleUrls: ["./heroes.component.css"]
})

export class HeroesComponent implements OnInit{

  selectedHero: Hero;
  heroes: Hero[];

  constructor(private heroService: HeroService, private router: Router) {}

  ngOnInit(): void {
    this.getHeroes();
  }

  getHeroes(): void {
    this.heroService.getHeroes().then(heroes=> this.heroes = heroes);
  }

  onSelect(h:Hero): void {
    this.selectedHero = h;
  }

  gotoDetail(): void {
    this.router.navigate(['/detail', this.selectedHero.id]);
  }
}


