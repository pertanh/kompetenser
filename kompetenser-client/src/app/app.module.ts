import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouteReuseStrategy } from '@angular/router';
import { FormsModule } from '@angular/forms';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import { IonicModule, IonicRouteStrategy } from '@ionic/angular';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { HttpClientModule } from '@angular/common/http';

import {AutoCompleteModule} from 'primeng/autocomplete';
import {CardModule} from 'primeng/card';
import {TableModule} from 'primeng/table';
import {PanelModule} from 'primeng/panel';

import { CompanyComponent } from './company/company.component';
import { EmploymentComponent } from './employment/employment.component';
import { CompetenceComponent } from './competence/competence.component';
import { CityComponent } from './city/city.component';

@NgModule({
  declarations: [
	AppComponent, CompanyComponent, EmploymentComponent, CompetenceComponent, CityComponent
  ],
  entryComponents: [],
  imports: [
	HttpClientModule, BrowserModule, IonicModule.forRoot(), 
	BrowserAnimationsModule, AppRoutingModule, FormsModule, 
	AutoCompleteModule, CardModule, TableModule, PanelModule
  ],
  providers: [
    StatusBar,
    SplashScreen,
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
