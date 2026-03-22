import { Injectable, signal } from '@angular/core';
import {  OAuthService } from 'angular-oauth2-oidc';
import { authConfig } from '../auth.config';
import { Router } from '@angular/router';
import { UserService } from '../../../features/users/service/user.service';

@Injectable({
  providedIn: 'root',
})

export class AuthService {
  

  isAuthenticated = signal(false);

  
  constructor(
    private oauthService: OAuthService,
    private userService: UserService,
    private router : Router 
    ) {}

  initLogin() {

    this.oauthService.configure(authConfig);

    this.oauthService.setupAutomaticSilentRefresh();

    this.oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      this.updateAuthState();

      if (this.oauthService.hasValidAccessToken()) {
        this.checkUserProfile();
      }else{
        this.login();
      }
    });

    this.oauthService.events.subscribe(() => {
      this.updateAuthState();
    });
  }

  checkUserProfile() {
    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        this.userService.loadUser();
        this.router.navigate(['/']);
      },
      error: () => {
        this.router.navigate(['/create-user']);
      }
    })
  }

  private updateAuthState() {
    this.isAuthenticated.set(
      this.oauthService.hasValidAccessToken()
    );
  }

  login(){
    this.oauthService.initCodeFlow();
  }

  logout(){
    this.oauthService.logOut();
  }

  getToken(){
    return this.oauthService.getAccessToken();
  }

  isLoggedIn(){
    return this.oauthService.hasValidAccessToken();
  }

  getUser() {
    const claims: any = this.oauthService.getIdentityClaims();
    console.log(claims);
  }

}
