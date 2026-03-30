import { TestBed } from '@angular/core/testing';

import { WalletServices } from './wallet.services';

describe('WalletServices', () => {
  let service: WalletServices;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WalletServices);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
