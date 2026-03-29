import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { AuthService } from './core/auth/service/auth.service';
import { HeaderComponent } from './shared/header/header.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ HeaderComponent,
    RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App  implements OnInit{
  protected readonly title = signal('frontend');

  private authService = inject(AuthService);

  ngOnInit(): void {
    this.authService.initLogin();
  }
}
