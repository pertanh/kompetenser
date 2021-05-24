import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {AutoCompleteModule} from 'primeng/autocomplete';

import { CompetenceComponent } from './competence.component';

@NgModule({
  imports: [ CommonModule, FormsModule, AutoCompleteModule ],
  declarations: [CompetenceComponent],
  exports: [CompetenceComponent]
})
export class CompetenceModule {}