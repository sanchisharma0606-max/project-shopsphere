/**
 * ShopSphere Client JavaScript
 * Interactive controls, image fallbacks, and micro-interactions
 */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Image Fallback Handler for remote / missing images
    const productImages = document.querySelectorAll('.product-img, .detail-img');
    productImages.forEach(img => {
        img.addEventListener('error', function() {
            // Generate clean inline SVG placeholder with product title
            const name = this.getAttribute('alt') || 'ShopSphere Product';
            this.src = `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="300" height="300" viewBox="0 0 300 300"><rect fill="%23f1f5f9" width="300" height="300"/><text fill="%2364748b" font-family="sans-serif" font-size="14" font-weight="bold" x="50%" y="45%" text-anchor="middle">ShopSphere</text><text fill="%2394a3b8" font-family="sans-serif" font-size="12" x="50%" y="55%" text-anchor="middle">${encodeURIComponent(name.substring(0, 25))}</text></svg>`;
        });
    });

    // 2. Quantity Selector Buttons (+ / -)
    const qtyWrappers = document.querySelectorAll('.qty-control-group');
    qtyWrappers.forEach(group => {
        const input = group.querySelector('.qty-input');
        const btnMinus = group.querySelector('.btn-qty-minus');
        const btnPlus = group.querySelector('.btn-qty-plus');

        if (input && btnMinus && btnPlus) {
            btnMinus.addEventListener('click', () => {
                let current = parseInt(input.value) || 1;
                if (current > 1) {
                    input.value = current - 1;
                    if (input.form) input.form.submit();
                }
            });

            btnPlus.addEventListener('click', () => {
                let current = parseInt(input.value) || 1;
                let max = parseInt(input.getAttribute('max')) || 99;
                if (current < max) {
                    input.value = current + 1;
                    if (input.form) input.form.submit();
                } else {
                    alert(`Maximum available stock is ${max}.`);
                }
            });
        }
    });

    // 3. Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            if (bsAlert) bsAlert.close();
        }, 5000);
    });
});
