import { Component, inject } from '@angular/core';
import { createWallet, WalletServices } from '../../services/wallet.services';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { UserService } from '../../../users/service/user.service';
import {  MatButtonModule } from '@angular/material/button';
import {  MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-wallet-create',
  imports: 
  [
       ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatSelectModule
  ],
  templateUrl: './wallet-create.component.html',
  styleUrl: './wallet-create.component.scss',
})
export class WalletCreateComponent {

  private walletService = inject(WalletServices);
  private fb = inject(FormBuilder);
  private toastr = inject(ToastrService);
  private router = inject(Router);
  private userService = inject(UserService);

  walletForm = this.fb.nonNullable.group({
  userId: [this.userService.currentUser()?.id ?? ''],
  type: ['', Validators.required],
  currency: ['', Validators.required],
  })

  accountTypes = ['USER_ACCOUNT', 'BANK_ACCOUNT', 'FEE_ACCOUNT'];
  currencies = ['USD', 'EUR', 'GBP', 'INR'];

  createWallet(){
    if(this.walletForm.invalid)requestAnimationFrame;

      const payload: createWallet = this.walletForm.getRawValue();

      this.walletService.createWalletAccount(payload).subscribe({
        next: () => {
          this.walletService.getAccount().subscribe({
            next: (wallet) => {
              this.walletService.currentWallet.set(wallet);
            }
          })
          this.walletService.isWalletContains= true;
          this.toastr.success('Wallet created', 'Success');
          this.router.navigate(['/']);
        },
        error: () => this.toastr.error('Failed to create wallet', 'Error')
      })
  }

}
