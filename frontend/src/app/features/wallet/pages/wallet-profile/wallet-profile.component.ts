import { Component, inject, OnInit } from '@angular/core';
import { responseBalance, WalletServices } from '../../services/wallet.services';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { DatePipe, CurrencyPipe } from '@angular/common';
import { NgClass } from '@angular/common';
import { UserService } from '../../../users/service/user.service';
@Component({
  selector: 'app-wallet-profile',
  imports: 
  [
      MatIconModule,
      MatButtonModule,
      RouterLink,
      DatePipe,
      CurrencyPipe,
      NgClass
  ],
  templateUrl: './wallet-profile.component.html',
  styleUrl: './wallet-profile.component.scss',
})
export class WalletProfileComponent implements OnInit {

  balance: responseBalance | null = null;
  public walletService = inject(WalletServices);
  public userService = inject(UserService);
  
  ngOnInit(): void {
    const walletId = this.walletService.currentWallet()?.id;
    if (walletId) {
      this.walletService.getCurrentBalance(walletId).subscribe({
        next: (bal) => this.balance = bal,
        error: () => this.balance = null
      });
    }
  }

}
