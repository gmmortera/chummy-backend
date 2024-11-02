type ValidationRules = (value: string) => boolean | string

export const useRules = () => {
  const email: ValidationRules[] = [
    value => {
      if (value) return true
      return 'E-mail is required.'
    },
    value => {
      if (/.+@.+\..+/.test(value)) return true
      return 'E-mail must be valid.'
    }
  ]

  const password: ValidationRules[] = [
    value => {
      if (value) return true
      return 'Password is required.'
    },
    value => {
      if (/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/.test(value)) return true
      return 'Password must be valid.'
    }
  ]

  const confirmation = (password: string) => {
    const rules: ValidationRules[] = [
      value => {
        if (value) return true
        return 'Confirm password.'
      },
      value => {
        if (value === password) return true
        return 'Password must be valid.'
      }
    ]
    return rules
  }

  return {
    email,
    password,
    confirmation
  }
}
