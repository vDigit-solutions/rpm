import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { HttpModule }    from '@angular/http';

import { AppComponent }  from './app.component';
import {ContractorDetailComponent} from "./contractor-detail.component";
import { ContractorsComponent } from "./contractors.component";
import {ContractorService} from "./contractor.service";
import {DashboardComponent} from "./dashboard.component";

import { AppRoutingModule }     from './app-routing.module';

@NgModule({
  imports:      [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpModule
  ],
  declarations: [
    AppComponent,
    ContractorDetailComponent,
    ContractorsComponent,
    DashboardComponent
  ],
  bootstrap:    [ AppComponent ],
  providers: [ContractorService],
})
export class AppModule { }
