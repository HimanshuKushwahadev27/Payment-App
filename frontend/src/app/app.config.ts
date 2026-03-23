import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideOAuthClient } from 'angular-oauth2-oidc';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/auth/interceptor/auth.interceptor';
import { idempotencyInterceptor } from './core/auth/interceptor/idempotency.interceptor';
import { provideToastr } from 'ngx-toastr';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';

export const appConfig: ApplicationConfig = {
  providers: [
    provideToastr({
    positionClass: 'toast-top-left',
    timeOut: 3000,
    closeButton: true,
    progressBar: true,
    preventDuplicates: true
    }),
    provideHttpClient(withInterceptors([authInterceptor, idempotencyInterceptor])),
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
        provideClientHydration(withEventReplay()),

    provideOAuthClient({
      resourceServer:{
        allowedUrls: ['https://localhost:8080'],
        sendAccessToken: true
      }
    })
  ]
};
