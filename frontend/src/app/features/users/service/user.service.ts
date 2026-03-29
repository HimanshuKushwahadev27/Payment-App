import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Observable } from 'rxjs';

export interface userProfile{
  id: string;
  email: string;
  name: string;
  profileImgUrl: string;
  phone : number;
  status ?: string;
  createdAt: Date;
}

export interface kycUser{
 adhaarNumber?: number;
 panNumber?: number;
}

export interface userUpdate{
  name: string;
  phone: number ;
}

export interface requestDocument{
  imgUrl: string;
  type: string;
}

export interface userRequest{
  name: string;
  phone: number;
  kycStatus: string;
}
@Injectable({
  providedIn: 'root',
})
export class UserService {
  
  currentUser = signal<userProfile | null>(null);

  private http = inject(HttpClient);

  getCurrentUser(): Observable<userProfile>{
    return this.http.get<userProfile>('/api/user/');
  }
  
  saveUploadUrl(request: requestDocument): Observable<string>{
    return this.http.post<string>('/api/user/file/save-url', request);
  }

  createUser(request: userRequest): Observable<userProfile>{
    return this.http.post<userProfile>('/api/user/create', request);
  }

  updateUser(request: userUpdate): Observable<userProfile>{
    return this.http.patch<userProfile>('/api/user/update', request);
  }

  validateKyc(request: kycUser): Observable<string>{
  return this.http.post('/api/user/kyc/submit', request, { 
    responseType: 'text' 
  });
}

  otpSend(phone: number): Observable<string>{
    return this.http.post('/api/user/kyc/otp/send', null, {
      params: { phone },
      responseType: 'text' as const
    }); 
  }

   otpVerify(phone: number, otp: string): Observable<string>{
    return this.http.post('/api/user/kyc/otp/verify', null, {
      params: { phone, otp },
      responseType: 'text' as const
    }); 
  }

  loadUser(){
    this.getCurrentUser().subscribe({
      next: (user) => this.currentUser.set(user),
      error: () => this.currentUser.set(null)
    })
  }
}
