import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import {  MatButtonModule } from '@angular/material/button';
import {  MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { userRequest, UserService } from '../../service/user.service';
import { raceWith } from 'rxjs';

@Component({
  selector: 'app-create-user',
  standalone: true,
  imports:
   [
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule
  ],
  templateUrl: './user-create.component.html',
  styleUrl: './user-create.component.scss',
})
export class UserCreateComponent {

  private fb = inject(FormBuilder);
  private http = inject(HttpClient);

  private userService = inject(UserService);

  userForm = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    profileImgUrl: ['', [Validators.required]],
    phone: ['', [Validators.required]],
    kycStatus: 'PENDING'
  });


  createUser(){
    if(this.userForm.invalid)return; 
      const raw = this.userForm.getRawValue();

    const payload: userRequest = {
      ...raw,
      phone: Number(raw.phone)
    };
    this.userService.createUser(payload);
  }

}
