import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Employment } from '../model/employment';

@Injectable({
  providedIn: 'root'
})
export class EmploymentService {
	
  private employmentUrl = 'http://localhost:8386/api/kompetenser/employees';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
  };

  constructor(private http: HttpClient) { }

  /** GET employees from the server */
  getEmployees(): Observable<Employment[]> {
    return this.http.get<Employment[]>(this.employmentUrl)
      .pipe(
        tap(_ => this.log('fetched employees')),
        catchError(this.handleError<Employment[]>('getEmployees', []))
      );
  }

  /* GET employees matching the parameters */
  getEmployeesFilter(companyId: number, cities: number[], comp: number[]): Observable<Employment[]> {
    if (companyId == null) {
      // if not search term, return empty employees array.
      return of([]);
    }
    let filterUrl = this.employmentUrl + `/${companyId}/0/10`;
    let hasCities: boolean = cities.length > 0;
    if (hasCities) {
	  filterUrl += `?city=${cities}`;
    }
    if (comp.length > 0) {
	  hasCities ? filterUrl += `&competence=${comp}` : filterUrl += `?competence=${comp}`;
    }
    return this.http.get<Employment[]>(`${filterUrl}`).pipe(
      tap(x => x.length ?
         this.log(`found employees matching "${cities}"`) :
         this.log(`no employees matching "${cities}"`)),
      catchError(this.handleError<Employment[]>('getEmployeesFilter', []))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a EmploymentService message with the MessageService */
  private log(message: string) {
	console.log(`EmploymentService: ${message}`);
    //this.messageService.add(`EmploymentService: ${message}`);
  }

}