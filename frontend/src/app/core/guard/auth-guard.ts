import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';



export const authGuard: CanActivateFn =  (route, state) => {
  const router = inject(Router);
  const  oauthService = inject(OAuthService);

  if (!oauthService.hasValidAccessToken()) {
   return router.createUrlTree(['/login']);
  }else{
    return true;
  }
  
};
