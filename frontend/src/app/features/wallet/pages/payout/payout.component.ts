import { Component, inject, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { PayoutService } from '../../services/payout.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-payout',
  imports: 
  [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './payout.component.html',
  styleUrl: './payout.component.scss',
})
export class PayoutComponent implements OnInit{

  private router = inject(Router);
  private payoutService = inject(PayoutService);

  ngOnInit(): void {
    this.payoutService.getStatus().subscribe({
      next: (wallet) => {
        this.payoutService.currentPayoutAccount.set(wallet);
      } 
    })

    if (this.payoutService.currentPayoutAccount()) {
      this.goToProfile();
    }
  }



  private toastr = inject(ToastrService);

  loading = false ;

  startOnboarding() {
    this.loading = true;

    this.payoutService.getOnboardingLink().subscribe({
      next: (url: string) => {
        window.location.href = url; 
      },
      error: () => {
        this.toastr.error('Failed to start onboarding', 'Error');
        this.loading = false;
      }
    });
  }

  goToProfile() {
  this.router.navigate(['/home/account-profile']);
  }

}
