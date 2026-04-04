import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';


export interface transactionResponse{
  id: string,
  toAccId: string,
  amt: number,
  currency: string,
  type: string,
  status: string,
  updatedAt: Date
}

export interface transactionRequest{
  recieversAccId: string,
  amount: string,
  currency: string
}

@Injectable({
  providedIn: 'root',
})
export class TransactionService {

  private http = inject(HttpClient);

  getTransactions(): Observable<transactionResponse []>{
    return this.http.get<transactionResponse []>('/api/transaction/');
  }

  getTransaction(transactionId: string): Observable<transactionResponse>{
    return this.http.get<transactionResponse>(`/api/transaction/get/${transactionId}`);
  }

  withdrawMoney(request: transactionRequest): Observable<transactionResponse>{
    return this.http.post<transactionResponse>('/api/transaction/payout', request);
  }

}
