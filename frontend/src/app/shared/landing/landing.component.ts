import { Component, inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../core/auth/service/auth.service';

@Component({
  selector: 'app-landing',
  imports: [MatButtonModule],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.scss',
})
export class LandingComponent implements OnInit{

  public authService = inject(AuthService);

  login(){
    this.authService.login();
  }

  ngOnInit(){
    setTimeout(() =>{
      this.login();
    }, 1500)
  }
}
