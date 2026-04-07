import {
  Component, OnInit, OnDestroy,
  inject, signal
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
 
import { MatButtonModule }          from '@angular/material/button';
import { MatInputModule }           from '@angular/material/input';
import { MatFormFieldModule }       from '@angular/material/form-field';
import { MatSelectModule }          from '@angular/material/select';
import { MatStepperModule }         from '@angular/material/stepper';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule }            from '@angular/material/icon';
import { MatDividerModule }         from '@angular/material/divider';
import { MatChipsModule }           from '@angular/material/chips';
import { MatCardModule }            from '@angular/material/card';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
 
import {
  loadStripe,
  Stripe,
  StripeElements,
  StripePaymentElement
} from '@stripe/stripe-js';
 
import { PaymentService } from '../../services/payment.service'; 
 
const STRIPE_PK = 'pk_test_YOUR_PUBLISHABLE_KEY';
 
type Step = 'amount' | 'payment' | 'success' | 'processing' | 'failed';

@Component({
  selector: 'app-charge',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatStepperModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatDividerModule,
    MatChipsModule,
    MatCardModule,
    MatSnackBarModule,
  ],
  templateUrl: './charge.component.html',
  styleUrl: './charge.component.scss',
})
export class ChargeComponent implements OnInit, OnDestroy {

  private svc = inject(PaymentService);
  private fb  = inject(FormBuilder);
  private snackbar = inject(MatSnackBar);
  router = inject(Router);

  step = signal<Step>('amount');
  loading = signal(false);
  errorMsg = signal('');

  private stripe : Stripe | null =null;
  private elements: StripeElements | null = null;
  private pelement: StripePaymentElement | null = null ;

  amountForm = this.fb.group({
    amount: [null as number | null, [Validators.required, Validators.min(1)]],
  })

  //quick money select 
  quickAmounts   = [10, 25, 50, 100, 250, 500];
  selectedQuick: number | null = null;

   
  setQuick(v: number) {
    this.amountForm.get('amount')!.setValue(v);
    this.selectedQuick = v;
  }
 
  clearQuick() {
    this.selectedQuick = null;
  }

  //payment method
  methods = [
    { value: 'CARD',         label: 'Credit / Debit Card', icon: 'credit_card'    },
    { value: 'BANK_ACCOUNT', label: 'Bank Account (ACH)',  icon: 'account_balance' },
  ];
  selectedMethod = 'CARD';
 
  get selectedMethodLabel(): string {
    return this.methods.find(m => m.value === this.selectedMethod)?.label ?? '';
  }

  get stepperIndex(): number {
    return this.step() === 'amount' ? 0 : 1;
  }

  async ngOnInit() {
    this.stripe = await loadStripe(STRIPE_PK);
  }
 
  ngOnDestroy() {
    this.pelement?.destroy();
  }

  initiateIntent(): void {
    if (this.amountForm.invalid || !this.stripe) return;

    this.loading.set(true);
    this.errorMsg.set('');

    const body = {
      amount:   this.amountForm.value.amount!,
      currency: 'USD',
      type:     this.selectedMethod,   
    };


    this.svc.deposit(body).subscribe({
      next: (res) => {
        this.step.set('payment');
        this.loading.set(false);
        // wait for Angular to render #payment-element div before mounting
        setTimeout(() => this.mountStripeElement(res['clientSecret']), 80);
      },
      error: (err) => {
        this.errorMsg.set(err?.error?.message ?? 'Could not initiate payment. Please try again.');
        this.loading.set(false);
      },
    });
  }


   private mountStripeElement(clientSecret: string): void {
    if (!this.stripe) return;
 
    this.elements = this.stripe.elements({
      clientSecret,
      appearance: {
        theme: 'stripe',
        variables: {
          colorPrimary:         '#7C3AED',
          colorBackground:      '#ffffff',
          colorText:            '#1a1a2e',
          colorDanger:          '#d32f2f',
          colorTextSecondary:   '#6b21a8',
          colorTextPlaceholder: '#9c27b0',
          fontFamily:           'Roboto, sans-serif',
          borderRadius:         '4px',
          spacingUnit:          '4px',
        },
        rules: {
          '.Input': {
            border:    '1px solid rgba(124,58,237,0.3)',
            boxShadow: 'none',
          },
          '.Input:focus': {
            border:    '2px solid #7C3AED',
            boxShadow: 'none',
          },
          '.Label': {
            color:         '#6b21a8',
            fontSize:      '12px',
            fontWeight:    '500',
            letterSpacing: '0.04em',
          },
          '.Tab': {
            border:          '1px solid rgba(124,58,237,0.2)',
            backgroundColor: '#faf5ff',
          },
          '.Tab--selected': {
            border:    '2px solid #7C3AED',
            color:     '#7C3AED',
            boxShadow: '0 2px 8px rgba(124,58,237,0.15)',
          },
          '.Error': { color: '#d32f2f' },
        },
      },
    });
 
    this.pelement = this.elements.create('payment', {
      layout: { type: 'tabs', defaultCollapsed: false },
    });
 
    this.pelement.mount('#payment-element');
  }


   async confirmPayment(): Promise<void> {
    if (!this.stripe || !this.elements) return;
 
    this.loading.set(true);
    this.errorMsg.set('');
 
    const { error, paymentIntent } = await this.stripe.confirmPayment({
      elements: this.elements,
      confirmParams: {
        // ACH redirects here after bank verification
        return_url: `${window.location.origin}/payment-return`,
      },
      // card: resolves without redirect
      // ACH: redirects to bank, comes back to /payment-return
      redirect: 'if_required',
    });
 
    if (error) {
      this.errorMsg.set(error.message ?? 'Payment failed. Please try again.');
      this.loading.set(false);
      return;
    }
  }
 
  backToAmount(): void {
    this.pelement?.destroy();
    this.pelement = null;
    this.elements  = null;
    this.errorMsg.set('');
    this.step.set('amount');
  }
 
  resetAll(): void {
    this.backToAmount();
    this.amountForm.reset();
    this.selectedMethod = 'CARD';
    this.selectedQuick  = null;
  }
 
  copyRef(ref: string): void {
    navigator.clipboard.writeText(ref);
    this.snackbar.open('Reference copied!', '', { duration: 2000 });
  }
}



