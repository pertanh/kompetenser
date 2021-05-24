import { ViewChild } from '@angular/core';
import { Component, OnInit } from '@angular/core';

import { Employment } from '../model/employment';
import { EmploymentService } from '../services/employment.service';
import { CityComponent } from '../city/city.component';
import { CompetenceComponent } from '../competence/competence.component';

@Component({
  selector: 'app-employment',
  templateUrl: './employment.component.html',
  styleUrls: ['./employment.component.scss'],
})
export class EmploymentComponent implements OnInit {
	
  @ViewChild(CityComponent)
  private cityComponent: CityComponent;

  @ViewChild(CompetenceComponent)
  private competenceComponent: CompetenceComponent;
	
  employees: Employment[];

  private cities: number[] = [];
  private comp: number[] = [];

  constructor(public employmentService: EmploymentService) { }

  ngOnInit() {
    this.getEmployees();
  }

  getEmployees(): void {
    this.employmentService.getEmployeesFilter(1, this.cities, this.comp)
    .subscribe(employees => this.employees = employees);
  }

  applyFilter(): void {
	this.cities = [];
	this.comp = [];

    for (let city of this.cityComponent.cities) {
      this.cities.push(city.id);
    } 
    for (let competence of this.competenceComponent.competences) {
      this.comp.push(competence.id);
    }

	this.getEmployees();
  }

}
