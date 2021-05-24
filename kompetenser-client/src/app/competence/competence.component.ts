import { Component, OnInit } from '@angular/core';

import { Competence } from '../model/competence';

import { CompetenceService } from '../services/competence.service';

@Component({
  selector: 'app-competence',
  templateUrl: './competence.component.html',
  styleUrls: ['./competence.component.scss'],
})
export class CompetenceComponent implements OnInit {
	
  competences: Competence[];

  filteredCompetences: Competence[];

  constructor(private service: CompetenceService) { }

  ngOnInit() {}

  getCompetences(event): void {
	let query = event.query;
    this.service.searchCompetence(query)
    .subscribe(res => this.filteredCompetences = this.filterCountry(query,res));
  }

  filterCountry(query, competences: Competence[]):Competence[] {
    //in a real application, make a request to a remote url with the query and return filtered results, for demo we filter at client side
    let filtered : Competence[] = [];
    for(let i = 0; i < competences.length; i++) {
      let comp = competences[i];
      if (comp.competence.toLowerCase().indexOf(query.toLowerCase()) == 0) {
        filtered.push(comp);
      }
    }
    return filtered;
  }

}
