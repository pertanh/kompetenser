import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { City } from '../model/city';

@Injectable({
  providedIn: 'root'
})
export class CityService {
	
  private cityUrl = 'http://localhost:8383/api/kompetenser/cities';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
  };

  constructor(private http: HttpClient) { }

  /* GET cities whose name contains search term */
  searchCity(term: string): Observable<City[]> {
    if (!term.trim()) {
      // if not search term, return empty competence array.
      return of([]);
    }
    return this.http.get<City[]>(`${this.cityUrl}/?name=${term}`).pipe(
      tap(x => x.length ?
         this.log(`found cities matching "${term}"`) :
         this.log(`no city matching "${term}"`)),
      catchError(this.handleError<City[]>('searchCity', []))
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

  /** Log a CityService message with the MessageService */
  private log(message: string) {
	console.log(`CityService: ${message}`);
    //this.messageService.add(`CityService: ${message}`);
  }

}
