import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {AutoCompleteModule} from 'primeng/autocomplete';

import { CityComponent } from './city.component';

@NgModule({
  imports: [ CommonModule, FormsModule, AutoCompleteModule ],
  declarations: [CityComponent],
  exports: [CityComponent]
})
export class CityModule {}