import { Component, computed, inject, OnInit } from '@angular/core';
import { PayoutService } from '../../services/payout.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { DatePipe, NgClass } from '@angular/common';

@Component({
  selector: 'app-account-profile',
  imports: 
  [
    MatIconModule,
    MatButtonModule,
    NgClass,
    DatePipe,
    RouterLink
  ],
  templateUrl: './account-profile.component.html',
  styleUrl: './account-profile.component.scss',
})
export class AccountProfileComponent implements OnInit{

  private route = inject(ActivatedRoute);
  private payoutService = inject(PayoutService);
  private toastr = inject(ToastrService);
  loading = false;

  ngOnInit(): void {
  const fromStripe = this.route.snapshot.queryParamMap.get('fromStripe');
  this.fetchStatus(fromStripe === 'true');
  }

  fetchStatus(fromStripe?: boolean) {
    this.payoutService.getStatus().subscribe({
      next: (data) => {
        this.payoutService.currentPayoutAccount.set(data);

        if (fromStripe) {
          if (data.payoutsEnabled) {
            this.toastr.success('🎉 Account setup complete!');
          } else {
            this.toastr.info('⏳ Verification in progress...');
          }
        }
      },
      error: () => {
        this.toastr.error('Failed to fetch account status');
      }
    });
 }
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
  payout = computed(() => this.payoutService.currentPayoutAccount());
}
