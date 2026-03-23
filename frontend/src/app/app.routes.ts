import { Routes } from '@angular/router';
import { authGuard } from './core/guard/auth-guard';

export const routes: Routes =
[

  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./shared/landing/landing.component').then(m => m.LandingComponent)
  },
  { 
    path: 'landing',
    canActivate: [authGuard],
    loadComponent: () => import('./shared/header/header.component').then(m => m.HeaderComponent)
  },

  { 
    path: 'create-user',
    canActivate: [authGuard],
    loadComponent: () => import('./features/users/pages/user-create/user-create.component').then(m => m.UserCreateComponent)
  }




];
