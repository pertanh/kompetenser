import { Component, OnInit, Input } from '@angular/core';

import { Company } from '../model/company';
import { CompanyService } from '../services/company.service';

@Component({
  selector: 'app-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.scss'],
})
export class CompanyComponent implements OnInit {
	
  @Input() company: Company;

  constructor(private service: CompanyService) { }

  ngOnInit(): void {
	this.getCompany(1);
  }

  getCompany(id: number): void {
    //const id = +this.route.snapshot.paramMap.get('id');
    this.service.getCompanyById(id)
      .subscribe(company => this.company = company);
  }

}