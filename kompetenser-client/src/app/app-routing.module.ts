import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';

import { CompanyComponent } from './company/company.component';
import { EmploymentComponent } from './employment/employment.component';
import { CompetenceComponent } from './competence/competence.component';
import { CityComponent } from './city/city.component';

const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./tabs/tabs.module').then(m => m.TabsPageModule)
  },
  { path: 'company', component: CompanyComponent },
  { path: 'employment', component: EmploymentComponent },
  { path: 'competence', component: CompetenceComponent },
  { path: 'city', component: CityComponent }
];
@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {}
