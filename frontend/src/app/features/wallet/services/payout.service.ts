import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Observable } from 'rxjs';


export interface payoutResponse{
  id: string,
  userKeycloakId: string,
  stripeAccountId: string,
  chargesEnabled: string,
  payoutsEnabled: string,
  bankName: string  ,
  last4: string,
  createdAt: string,
  updatedAt: string
}

@Injectable({
  providedIn: 'root',
})
export class PayoutService {

  private http = inject(HttpClient);
  currentPayoutAccount = signal<payoutResponse | null>(null);

  

  getStatus(): Observable<payoutResponse>{
    return this.http.get<payoutResponse>('/api/wallet/stored_accounts/status');
  }

  getOnboardingLink(): Observable<string> {
  return this.http.get('/api/wallet/stored_accounts/onboarding-link', { responseType: 'text' });
  }
}

