import { Component } from '@angular/core';
import { HeaderButtonComponent } from '../header-button/header-button.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { RouterLink} from '@angular/router';


@Component({
  selector: 'app-header',
  standalone: true,
  imports:
  [
    
     HeaderButtonComponent,
     MatIconModule,
     MatInputModule,
     MatFormFieldModule,
     MatToolbarModule,
     MatButtonModule,
     ReactiveFormsModule,
     MatAutocompleteModule,
     RouterLink
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent {}
