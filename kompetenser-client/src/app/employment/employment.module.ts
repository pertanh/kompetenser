import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


import { EmploymentComponent } from './employment.component';

@NgModule({
  imports: [ CommonModule, FormsModule ],
  declarations: [EmploymentComponent],
  exports: [EmploymentComponent]
})
export class EmploymentModule {}
