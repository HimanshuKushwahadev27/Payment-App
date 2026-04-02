import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideToastr } from 'ngx-toastr';
import { routes } from './app.routes';
import { provideOAuthClient } from 'angular-oauth2-oidc';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/auth/interceptor/auth.interceptor';
import { idempotencyInterceptor } from './core/auth/interceptor/idempotency.interceptor';
export const appConfig: ApplicationConfig = {
  providers: [
    provideToastr({
      positionClass: 'toast-top-right',
      timeOut: 3000,
      closeButton: true,
      progressBar: true,
      preventDuplicates: true,
    }),
    provideHttpClient(withInterceptors([authInterceptor, idempotencyInterceptor])),
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideOAuthClient({
      resourceServer:{
        allowedUrls: ['http://localhost:8081'],
        sendAccessToken: true
      }
    })
  ]
};
