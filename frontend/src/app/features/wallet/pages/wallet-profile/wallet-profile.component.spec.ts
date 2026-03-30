import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WalletProfileComponent } from './wallet-profile.component';

describe('WalletProfileComponent', () => {
  let component: WalletProfileComponent;
  let fixture: ComponentFixture<WalletProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WalletProfileComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(WalletProfileComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
