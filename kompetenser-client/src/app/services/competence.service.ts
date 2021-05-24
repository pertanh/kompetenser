import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Competence } from '../model/competence';

@Injectable({
  providedIn: 'root'
})
export class CompetenceService {
	
  private competencetUrl = 'http://localhost:8385/api/kompetenser/competences';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
  };

  constructor(private http: HttpClient) { }

  /* GET competences whose name contains search term */
  searchCompetence(term: string): Observable<Competence[]> {
    if (!term.trim()) {
      // if not search term, return empty competence array.
      return of([]);
    }
    return this.http.get<Competence[]>(`${this.competencetUrl}/?name=${term}`).pipe(
      tap(x => x.length ?
         this.log(`found competences matching "${term}"`) :
         this.log(`no competences matching "${term}"`)),
      catchError(this.handleError<Competence[]>('searchCompetence', []))
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

  /** Log a CompetenceService message with the MessageService */
  private log(message: string) {
	console.log(`CompetenceService: ${message}`);
    //this.messageService.add(`CompetenceService: ${message}`);
  }

}
