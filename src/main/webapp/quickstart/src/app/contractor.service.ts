import { Injectable } from '@angular/core';
import {CONTRACTORS} from "./mock-heroes";
import {Contractor} from "./contractor";

@Injectable()
export class ContractorService {

  getHeroes(): Promise<Contractor[]> {
    return new Promise(resolve => {
      // Simulate server latency with 2 second delay
      setTimeout(() => resolve(CONTRACTORS), 500);
    });
  }

  getHero(id: number): Promise<Contractor> {
    return this.getHeroes()
      .then(contractors => contractors.find(contractor => contractor.id === id));
  }
}
