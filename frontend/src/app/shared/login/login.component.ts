import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { OAuthService } from 'angular-oauth2-oidc';
import { AuthService } from '../../core/auth/service/auth.service';

@Component({
  selector: 'app-login',
  imports: [MatButtonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {

  private oauth = inject(AuthService);

  login() {
    this.oauth.login();
  }
}