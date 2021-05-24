import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {CardModule} from 'primeng/card';


import { CompanyComponent } from './company.component';
import { CityComponent } from '../city/city.component';

@NgModule({
  imports: [ CommonModule, FormsModule ],
  declarations: [CompanyComponent],
  exports: [CompanyComponent]
})
export class CompetenceModule {}