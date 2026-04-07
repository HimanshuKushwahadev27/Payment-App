import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface paymentRequest{
  amount: number,
  currency: string,
  type: string
}

@Injectable({
  providedIn: 'root',
})
export class PaymentService {

  private http = inject(HttpClient);

  deposit(request: paymentRequest): Observable<{ [key: string]: string }>{
    return this.http.post<{ [key: string]: string }>('/api/payment/charge', request);
  }

  

}
