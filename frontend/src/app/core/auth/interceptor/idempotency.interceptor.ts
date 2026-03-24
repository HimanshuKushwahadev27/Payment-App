import { HttpInterceptorFn } from '@angular/common/http';

export const idempotencyInterceptor: HttpInterceptorFn = (req, next) => {

  const isPost = req.method === 'POST';
  const isApiRequest = req.url.startsWith('/api') || req.url.includes('/api/');

  if (isPost && isApiRequest) {

    const cloned = req.clone({
      setHeaders: {
        'Idempotency-Key': Date.now().toString()
      }
    });

    return next(cloned);
  }

  return next(req);
};