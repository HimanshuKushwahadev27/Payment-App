import { Routes } from '@angular/router';
import { authGuard } from './core/guard/auth-guard';
import { walletGuard } from './core/guard/wallet-guard';

export const routes: Routes =
[
  { 
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./shared/home/home.component').then(m => m.HomeComponent)
  },

  { 
    path: 'create-user',
    canActivate: [authGuard],
    loadComponent: () => import('./features/users/pages/user-create/user-create.component').then(m => m.UserCreateComponent)
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./shared/login/login.component')
        .then(m => m.LoginComponent)
  },
  { 
    path: 'home/user-profile',
    canActivate: [authGuard],
    loadComponent: () => import('./features/users/pages/user-profile/user-profile.component').then(m => m.UserProfileComponent)
  },
  { 
    path: 'home/user-profile/update',
    canActivate: [authGuard],
    loadComponent: () => import('./features/users/pages/user-update/user-update.component').then(m => m.UserUpdateComponent)
  },
  { 
    path: 'home/user-profile/kyc',
    canActivate: [authGuard],
    loadComponent: () => import('./features/users/pages/kyc/kyc.component').then(m => m.KycComponent)
  },
  { 
    path: 'home/wallet-create',
    canActivate: [authGuard],
    loadComponent: () => import('./features/wallet/pages/wallet-create/wallet-create.component').then(m => m.WalletCreateComponent)
  },

  { 
    path: 'home/wallet-profile',
    canActivate: [authGuard],
    loadComponent: () => import('./features/wallet/pages/wallet-profile/wallet-profile.component').then(m => m.WalletProfileComponent)
  },

  { 
    path: 'home/transfer',
    canActivate: [authGuard, walletGuard],
    loadComponent: () => import('./features/wallet/pages/transfer/transfer.component').then(m => m.TransferComponent)
  },

  { 
    path: 'home/payout-account',
    canActivate: [authGuard, walletGuard],
    loadComponent: () => import('./features/wallet/pages/payout/payout.component').then(m => m.PayoutComponent)
  },

  { 
    path: 'success',
    canActivate: [authGuard, walletGuard],
    loadComponent: () => import('./features/wallet/pages/account-profile/account-profile.component').then(m => m.AccountProfileComponent)
  },

];
