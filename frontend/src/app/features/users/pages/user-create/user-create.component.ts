import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import {  MatButtonModule } from '@angular/material/button';
import {  MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { requestDocument, userRequest, UserService } from '../../service/user.service';
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

  previewUrl: string | null = null;
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);

  private userService = inject(UserService);

  userForm = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
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


  onDragOver(event: DragEvent) {
   event.preventDefault();
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    const file = event.dataTransfer?.files[0];
    if (file) {
      this.handleFile(file);
    }
  }

  onFileSelect(event: any) {
     const file = event.target.files[0];
    if (file) {
      this.handleFile(file);
    }
  }

  handleFile(file: File) {

    this.previewUrl = URL.createObjectURL(file);

    this.http.get<string>(`/api/user/file/upload-url?fileName=${file.name}`)
      .subscribe((uploadUrl) => {

        this.http.put(uploadUrl, file, {
          headers: { 'Content-Type': file.type }
        }).subscribe(() => {

          const cleanUrl = uploadUrl.split('?')[0];

          const payload: requestDocument = {
            imgUrl: cleanUrl,
            type: 'PROFILE_IMAGE'
          }

          this.userService.saveUploadUrl(payload);

          console.log('Upload complete');
        });

      });
  }

}
