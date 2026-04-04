import { Injectable } from '@angular/core';

export interface paymentRequest{
  amount: number,
  currency: string,
  type: string
}

@Injectable({
  providedIn: 'root',
})
export class PaymentService {

}
