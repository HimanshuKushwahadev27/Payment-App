import { Injectable } from '@angular/core';


export interface createPayoutAccount{
  destinationAccountId: string,
  bankName: string,
  last4: string,
  isDefault: boolean
}

export interface payoutResponse{
  id: string,
  userKeycloakId: string,
  destinationAccountId : string,
  bankName: string  ,
  last4: string,
  isDefault: string,
  createdAt: string,
  updatedAt: string
}

@Injectable({
  providedIn: 'root',
})
export class PayoutService {

  

}
