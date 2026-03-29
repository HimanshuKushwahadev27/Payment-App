
import { Component, inject, OnInit, Output, EventEmitter} from '@angular/core';
import { AuthService } from '../../core/auth/service/auth.service';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {MatDividerModule} from '@angular/material/divider';
import { UserService } from '../../features/users/service/user.service';
import { RouterLink } from '@angular/router';

import { MatCardHeader } from '@angular/material/card';
import { MatCardTitle } from '@angular/material/card';
@Component({
  selector: 'app-header-button',
  standalone: true,
  imports: 
  [
    MatIconModule,
    MatButtonModule ,
    MatMenuModule,
    MatDividerModule,
    MatCardHeader,
    MatCardTitle,
    RouterLink
  ],
  templateUrl: './header-button.component.html',
  styleUrl: './header-button.component.scss',
})
export class HeaderButtonComponent {

  constructor(public authService: AuthService, public userService: UserService) {}

  logout(){
    this.authService.logout();
  }

  avatarFallback(event: Event) {
    (event.target as HTMLImageElement).src = 'assets/default-avatar.png';
  }

}
