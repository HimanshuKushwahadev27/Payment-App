import { Routes } from '@angular/router';
import { authGuard } from './core/guard/auth-guard';

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



];
