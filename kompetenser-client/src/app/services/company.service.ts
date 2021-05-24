import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Company } from '../model/company';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  private companyUrl = 'http://localhost:8384/api/kompetenser/companies';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
  };

  constructor(private http: HttpClient) { }

  /** GET hero by id. Will 404 if id not found */
  getCompanyById(companyId: number): Observable<Company> {
    const url = `${this.companyUrl}/${companyId}`;
    return this.http.get<Company>(url).pipe(
      tap(_ => this.log(`fetched company id=${companyId}`)),
      catchError(this.handleError<Company>(`getCompanyById companyId=${companyId}`))
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

  /** Log a CompanyService message with the MessageService */
  private log(message: string) {
	console.log(`CompanyService: ${message}`);
    //this.messageService.add(`CompanyService: ${message}`);
  }

}