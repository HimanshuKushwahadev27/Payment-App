import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { UserService } from '../../features/users/service/user.service';
import { catchError, map, of } from 'rxjs';


export const authGuard: CanActivateFn =  (route, state) => {
  const userService = inject(UserService);
  const router = inject(Router);
  const  oauthService = inject(OAuthService);

  if (!oauthService.hasValidAccessToken()) {
   return router.createUrlTree(['/login']);
  }else{
    return true;
  }
  
};
