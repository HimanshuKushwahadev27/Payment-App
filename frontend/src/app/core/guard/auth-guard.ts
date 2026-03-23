import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../auth/service/auth.service';

export const authGuard: CanActivateFn = async () => {
  const authService = inject(AuthService);

  const router =inject(Router);

  if(!authService.isAuthReady()){
    await authService.isLoggedIn();
  }

  if (authService.isLoggedIn()) {
    return true;
  }
  
  return router.createUrlTree(['/landing']);
};
