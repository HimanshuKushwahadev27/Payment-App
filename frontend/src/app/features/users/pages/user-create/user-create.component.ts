import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import {  MatButtonModule } from '@angular/material/button';
import {  MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { requestDocument, userRequest, UserService } from '../../service/user.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

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

  pendingFile: File | null =null;
  previewUrl: string | null = null;
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private toastr = inject(ToastrService);
  private userService = inject(UserService);
  private router = inject(Router);

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
    this.userService.createUser(payload).subscribe({
      next: () => {
          this.toastr.success('User profile completed successfully', 'Success');
            if(this.pendingFile) {
                this.handleFile(this.pendingFile);
            } else {
                this.router.navigate(['/']);
            }
      },
          error: () => this.toastr.error('Failed to create user profile', 'Error')
    })
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
      this.previewUrl= URL.createObjectURL(file);
      this.pendingFile= file;
    }
  }

 handleFile(file: File) {
     const formData = new FormData();
    formData.append('fileName', file);

    this.http.post('/api/user/file/upload-url', formData, { responseType: 'text' })
        .subscribe({
            next: (fileUrl) => {
                const payload: requestDocument = {
                    imgUrl: fileUrl,
                    type: 'PROFILE_IMAGE'
                };
                this.userService.saveUploadUrl(payload).subscribe({
                    next: () => {
                        console.log('Upload complete');
                        this.router.navigate(['/']);
                        this.userService.loadUser();
                    },
                    error: (err) => console.error('Save URL failed:', err)
                });
            },
            error: (err) => console.error('Upload failed:', err)
        });
}

}
