import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { WalletServices } from '../../features/wallet/services/wallet.services';

export const walletGuard: CanActivateFn = (route, state) => {

  const walletService = inject(WalletServices)
  const router = inject(Router);

  

  if(walletService.currentWallet()){
    return router.createUrlTree(['/home/wallet-create'])
  }else{
    return true;
  }
};
