import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth/service/auth.service';

@Component({
  selector: 'app-home',
  imports:
   [
    RouterLink,
    MatIconModule,
    MatButtonModule 
   ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent {

    authService = inject(AuthService);

}
