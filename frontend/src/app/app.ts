import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './core/auth/service/auth.service';
import { UserCreateComponent } from './features/users/pages/user-create/user-create.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, UserCreateComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  protected readonly title = signal('frontend');

  private oauthService = inject(AuthService);


  ngOnInit(): void {
    this.oauthService.initLogin();
  }

}
