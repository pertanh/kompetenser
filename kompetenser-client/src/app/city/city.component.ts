import { Component, OnInit } from '@angular/core';

import { City } from '../model/city';

import { CityService } from '../services/city.service';

@Component({
  selector: 'app-city',
  templateUrl: './city.component.html',
  styleUrls: ['./city.component.scss'],
})
export class CityComponent implements OnInit {

  cities: City[];

  filteredCities: City[];

  constructor(private service: CityService) { }

  ngOnInit() {}

  getCities(event): void {
	let query = event.query;
    this.service.searchCity(query)
    .subscribe(res => this.filteredCities = this.filterCity(query,res));
  }

  filterCity(query, cities: City[]):City[] {
    //in a real application, make a request to a remote url with the query and return filtered results, for demo we filter at client side
    let filtered : City[] = [];
    for(let i = 0; i < cities.length; i++) {
      let city = cities[i];
      if (city.name.toLowerCase().indexOf(query.toLowerCase()) == 0) {
        filtered.push(city);
      }
    }
    return filtered;
  }

}