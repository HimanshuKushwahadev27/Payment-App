import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component, inject, NgModule, OnInit, signal } from '@angular/core';
import { FormBuilder, FormsModule, NgModel, ReactiveFormsModule, Validators } from '@angular/forms';
import {  MatButtonModule } from '@angular/material/button';
import {  MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { requestDocument, kycUser, UserService } from '../../service/user.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-kyc',
  standalone: true,
  imports: 
  [
      ReactiveFormsModule,
      MatFormFieldModule,
      MatInputModule,
      MatButtonModule,
      FormsModule
  ],
  templateUrl: './kyc.component.html',
  styleUrl: './kyc.component.scss',
})
export class KycComponent implements OnInit {

  public showOtpInput = signal(false);
  private phone : number | null = null;
  pendingFile: File | null =null;
  previewUrl: string | null = null;
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private toastr = inject(ToastrService);
  private userService = inject(UserService);
  private router = inject(Router);
  public otpValue: string | null = null;
  private cdr = inject(ChangeDetectorRef)

 userForm = this.fb.nonNullable.group({
    adhaarNumber: [''],
    panNumber: ['']
  });

  ngOnInit(): void {
      const adhaarControl = this.userForm.get('adhaarNumber');
      const panControl = this.userForm.get('panNumber');
      adhaarControl?.valueChanges.subscribe((value: string) => {
        if (value && value.trim() !== '') {
          panControl?.disable({ emitEvent: false });
        } else {
          panControl?.enable({ emitEvent: false });
        }
      });

      panControl?.valueChanges.subscribe((value: string) => {
        if (value && value.trim() !== '') {
          adhaarControl?.disable({ emitEvent: false });
        } else {
          adhaarControl?.enable({ emitEvent: false });
        }
      });
  }

 validateUser() {
  console.log('validateUser called');    
  console.log('Form valid:', this.userForm.valid);
  console.log('Form value:', this.userForm.value);
  if (this.userForm.invalid) return;

  const raw = this.userForm.getRawValue();

  const payload: kycUser = {
    adhaarNumber: raw.adhaarNumber ? Number(raw.adhaarNumber) : undefined,
    panNumber: raw.panNumber ? Number(raw.panNumber) : undefined
  };
  this.userService.validateKyc(payload).subscribe({
    next: (res) => {
      this.toastr.success('KYC information saved', 'Success');

      console.log('Payload:', payload);
      const phone = this.userService.currentUser()?.phone;
      if (!phone) {
        this.toastr.error('Phone number not found', 'Error');
        return;
      }

      this.userService.otpSend(phone).subscribe({
        next: () => {
          this.toastr.info('OTP sent to your phone', 'OTP');
          this.phone = phone;
          this.showOtpInput.set(true); 
          this.cdr.detectChanges();
        },
        error: (err) =>
           this.toastr.error('Failed to send OTP', 'Error')
      });
    },
    error: () => this.toastr.error('Failed to validate KYC', 'Error')
  });
}

verifyOtp() {
    if (!this.phone || !this.otpValue) {
    this.toastr.error('Phone number not found', 'Error');
    return;
  }
  this.userService.otpVerify(this.phone, this.otpValue).subscribe({
    next: () => {
      this.toastr.success('OTP verified', 'Success');
      if (this.pendingFile) {
        this.handleFile(this.pendingFile); 
      } else {
        this.router.navigate(['/']);
        this.userService.loadUser();
      }
    },
    error: () => this.toastr.error('Invalid OTP', 'Error')
  });
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
                    type: 'DOCUMENT'
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
