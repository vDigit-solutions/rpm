import { Injectable } from '@angular/core';
// import {CONTRACTORS} from "./mock-heroes";
import {Contractor} from "./contractor";
import {Http} from "@angular/http";
import 'rxjs/add/operator/toPromise';

@Injectable()
export class ContractorService {

  private contractorsServiceUrl = "http://localhost:8080/contractors";

  constructor(private http:Http) {}

  getHeroes(): Promise<Contractor[]> {
    // return new Promise(resolve => {
    //   // Simulate server latency with 2 second delay
    //   setTimeout(() => resolve(CONTRACTORS), 500);
    // });

    console.log("getting contractorss")

    return this.http.get(this.contractorsServiceUrl)
      .toPromise()
      .then(resp => {
          console.log("contractos response is:%s", JSON.stringify(resp));
          return resp.json() as Contractor[];
        }
      )
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

  getHero(id: number): Promise<Contractor> {
    return this.getHeroes()
      .then(contractors => contractors.find(contractor => contractor.id === id));
  }
}
