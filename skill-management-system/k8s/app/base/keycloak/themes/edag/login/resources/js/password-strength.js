/**
 * Simplified Password Strength Indicator for EDAG Theme
 */

(function() {
  'use strict';

  // Password requirements
  const REQUIREMENTS = {
    length: {
      id: 'req-length',
      test: (password) => password.length >= 8,
      text: 'At least 8 characters'
    },
    uppercase: {
      id: 'req-uppercase',
      test: (password) => /[A-Z]/.test(password),
      text: 'One uppercase letter'
    },
    lowercase: {
      id: 'req-lowercase',
      test: (password) => /[a-z]/.test(password),
      text: 'One lowercase letter'
    },
    number: {
      id: 'req-number',
      test: (password) => /\d/.test(password),
      text: 'One number'
    },
    special: {
      id: 'req-special',
      test: (password) => /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password),
      text: 'One special character'
    }
  };

  /**
   * Calculate password strength score
   */
  function calculateStrength(password) {
    if (!password) return 0;

    let score = 0;

    // Check each requirement
    Object.values(REQUIREMENTS).forEach(req => {
      if (req.test(password)) {
        score++;
      }
    });

    // Convert to 0-4 scale
    if (score >= 5) return 4; // Very Strong
    if (score >= 4) return 3; // Strong
    if (score >= 3) return 2; // Good
    if (score >= 2) return 1; // Fair
    return 0; // Weak
  }

  /**
   * Get strength level
   */
  function getStrengthLevel(score) {
    const levels = ['weak', 'fair', 'good', 'strong', 'very-strong'];
    const texts = ['Weak', 'Fair', 'Good', 'Strong', 'Very Strong'];
    return { class: levels[score] || 'weak', text: texts[score] || 'Weak' };
  }

  /**
   * Update password strength indicator
   */
  function updateStrengthIndicator(password) {
    const strengthFill = document.getElementById('password-strength-fill');
    const strengthText = document.getElementById('password-strength-text');

    if (!strengthFill || !strengthText) return;

    const score = calculateStrength(password);
    const level = getStrengthLevel(score);

    // Update strength bar
    strengthFill.className = `kc-password-strength-fill ${level.class}`;

    // Update text
    strengthText.textContent = level.text;
    strengthText.className = `kc-password-strength-text ${level.class}`;

    // Update requirements
    updateRequirements(password);
  }

  /**
   * Update individual requirements
   */
  function updateRequirements(password) {
    Object.entries(REQUIREMENTS).forEach(([key, req]) => {
      const element = document.getElementById(req.id);
      if (element) {
        const isMet = req.test(password);
        element.classList.toggle('met', isMet);

        const icon = element.querySelector('.kc-requirement-icon');
        if (icon) {
          icon.textContent = isMet ? '✓' : '✗';
        }
      }
    });
  }

  /**
   * Check password match
   */
  function checkPasswordMatch(password, confirm) {
    const matchIndicator = document.getElementById('password-match');
    if (!matchIndicator) return;

    if (confirm.length === 0) {
      matchIndicator.textContent = '';
      matchIndicator.className = 'kc-password-match';
    } else if (password === confirm) {
      matchIndicator.textContent = '✓ Passwords match';
      matchIndicator.className = 'kc-password-match kc-match-success';
    } else {
      matchIndicator.textContent = '✗ Passwords do not match';
      matchIndicator.className = 'kc-password-match kc-match-error';
    }
  }

  /**
   * Initialize password strength indicator
   */
  function initPasswordStrength() {
    const passwordInput = document.getElementById('password');
    const confirmInput = document.getElementById('password-confirm');

    if (!passwordInput) return;

    // Password input handler
    passwordInput.addEventListener('input', function() {
      updateStrengthIndicator(this.value);
      if (confirmInput && confirmInput.value) {
        checkPasswordMatch(this.value, confirmInput.value);
      }
    });

    // Confirm input handler
    if (confirmInput) {
      confirmInput.addEventListener('input', function() {
        const password = passwordInput.value;
        checkPasswordMatch(password, this.value);

        // Set custom validity
        if (this.value && password !== this.value) {
          this.setCustomValidity('Passwords do not match');
        } else {
          this.setCustomValidity('');
        }
      });
    }

    // Initial update
    updateStrengthIndicator(passwordInput.value);
  }

  /**
   * Initialize when DOM is ready
   */
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initPasswordStrength);
  } else {
    initPasswordStrength();
  }
})();
