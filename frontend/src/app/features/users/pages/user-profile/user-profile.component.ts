import { Component, inject } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { UserService } from '../../service/user.service';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { DatePipe, NgClass } from '@angular/common';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: 
  [
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    RouterLink,
    DatePipe,
    NgClass,
    RouterLink
  ],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.scss',
})
export class UserProfileComponent {

  public userService = inject(UserService);

  getStatusClass(): string {
    const status = this.userService.currentUser()?.status;
    return status ? status.toLowerCase() : '';
  }
  getKycIcon(): string {
  const status = this.userService.currentUser()?.status;

   switch (status) {
      case 'VERIFIED': return 'verified';   // ✔️
      case 'PENDING': return 'schedule';    // ⏳
      case 'REJECTED': return 'error';      // ❌
      default: return 'help';
    }
  }
}
