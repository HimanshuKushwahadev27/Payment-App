import { Component, inject, OnInit } from '@angular/core';
import { responseBalance, WalletServices } from '../../services/wallet.services';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { DatePipe, CurrencyPipe } from '@angular/common';
import { UserService } from '../../../users/service/user.service';
@Component({
  selector: 'app-wallet-profile',
  imports: 
  [
      MatIconModule,
      MatButtonModule,
      RouterLink,
      DatePipe,
      CurrencyPipe
  ],
  templateUrl: './wallet-profile.component.html',
  styleUrl: './wallet-profile.component.scss',
})
export class WalletProfileComponent implements OnInit {

  balance: responseBalance | null = null;
  public walletService = inject(WalletServices);
  public userService = inject(UserService);
  
  ngOnInit(): void {
      this.walletService.getCurrentBalance().subscribe({
        next: (bal) => this.balance = bal,
        error: () => this.balance = null
      });
  }

  getAccount(){
    this.walletService.isWalletContains = true;
      if(!this.walletService.currentWallet()){
          this.walletService.getAccount().subscribe({
              next: (wallet) => {
               this.walletService.currentWallet.set(wallet);
       }
     })
    }
  }

}
