import {Component, OnInit} from '@angular/core';
import {Contractor} from "./contractor";
import { ContractorService } from './contractor.service';
import {Router} from "@angular/router";


@Component({
  selector: 'my-heroes',
  templateUrl: "./contractors.component.html",
  styleUrls: ["./contractors.component.css"]
})

export class ContractorsComponent implements OnInit{

  selectedHero: Contractor;
  contractors: Contractor[];

  constructor(private contractorService: ContractorService, private router: Router) {}

  ngOnInit(): void {
    this.getHeroes();
  }

  getHeroes(): void {
    this.contractorService.getHeroes().then(heroes=> {
      console.log("Returned from Hero Service: %s", JSON.stringify(heroes));
      this.contractors = heroes
    });
  }

  onSelect(h:Contractor): void {
    this.selectedHero = h;
  }

  gotoDetail(): void {
    this.router.navigate(['/detail', this.selectedHero.id]);
  }
}


