import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';

export interface userProfile{
  id: string;
  email: string;
  name: string;
  profileImgUrl: string;
  phone ?: number;
  status ?: string;
  createdAt: Date;
}

export interface userRequest{
  name: string;
  profileImgUrl: string;
  phone: string;
  kycStatus: string;
}
@Injectable({
  providedIn: 'root',
})
export class UserService {
  
  currentUser = signal<userProfile | null>(null);

  private toastr = inject(ToastrService)
  private http = inject(HttpClient);
  private router = inject(Router);

  getCurrentUser(): Observable<userProfile>{
    return this.http.get<userProfile>('/api/user/');
  }


  createUser(request: userRequest){
    return this.http.post<userProfile>('/api/user/create', request)
      .subscribe({
       next: () =>{
       this.toastr.success(
        'User profile completed successfully',
        'Success'
      );
      this.router.navigate(['/']);
    },
         error: (err) => {
      this.toastr.error(
        'Failed to create user profile',
        'Error'
      );
    }
      })
  }

  

  loadUser(){
    this.getCurrentUser().subscribe({
      next: (user) => this.currentUser.set(user),
      error: () => this.currentUser.set(null)
    })
  }
}
