import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Observable } from 'rxjs';



export interface createWallet{
  userId: string;
  type: string;
  currency: string;
}



export interface transferAmountDto{
  toAccountId: string;
  fromAccountId: string;
  amount: number;
}

export interface responseBalance{
  balance: number;
  updateAt: Date;
}
export interface responseWallet{
  id: string;
  createdAt: Date;
  currency: string;
  type: string;
}

@Injectable({
  providedIn: 'root',
})

export class WalletServices {

  currentWallet = signal<responseWallet | null>(null);

  private http = inject(HttpClient);

  getCurrentBalance(id: string): Observable<responseBalance>{
    return this.http.get<responseBalance>(`/api/wallet/account/${id}`);
  }

  createWalletAccount(request: createWallet): Observable<responseWallet>{
    return this.http.post<responseWallet>('/api/wallet/account/create', request);
  }

  transferAmount(request: transferAmountDto){
    return this.http.post('/api/wallet/account/transfer', request);
  }
}
