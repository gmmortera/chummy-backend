<template>
  <div class="register">
    <section class="register__left">
      <v-img
        contain
        :width="144"
        src="/assets/logo-with-circle.svg"
      />
    </section>
    <section class="register__right">
        <v-form>
          <v-container>
            <v-text-field
              v-model="registerForm.email" 
              :rules="rules.email"
              label="Email"
              type="email"
              required
            ></v-text-field>
            <v-text-field
              v-model="registerForm.password"
              :rules="rules.password"
              label="Password"
              type="password"
              required
            ></v-text-field>
            <v-text-field
              v-model="registerForm.confirmation"
              :rules="rules.confirmation(registerForm.password)"
              label="Confirm Password"
              type="password"
              required
            ></v-text-field>
            <v-btn
              @click="register"
            >
              Register
            </v-btn>
            <p>Already have an account? <router-link class="login" :to="{ path: '/login' }">Sign in here</router-link></p>
          </v-container>
        </v-form>
    </section>
  </div>
</template>

<script lang="ts" setup>
  import { useRules } from '../composables/rules'

  const registerForm = reactive({
    email: '',
    password: '',
    confirmation: ''
  })

  const { post } = useUserStore()
  const { push } = useRouter()
  const rules = useRules()

  const register = async () => {
    try {
      const { confirmation, ...rest } = registerForm
      const status = await post(rest)
      
      if (status === 201) {
        push('/')
      }
    } catch (error) {
      const e = error as Error
      console.error(e)
    }
  }
</script>

<style lang="scss" scoped>
.register {
    display: flex;
    justify-content: space-between;
    height: 100vh;
    background: $primary;

    &__left {
      width: 60%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      overflow: hidden;
      color: $bg;
    }

    &__right {
      width: 40%;
      background: $bg;
      border-radius: 10% 0 0 10%;

      .logo {
        display: inline-flex;
        margin-top: 1rem;
        padding-right: 1.5rem;

        h1 {
          font-size: 1.8rem;
          color: $primary;
        }
      }

      .v-form {
        max-width: 25rem;
        margin-left: 5em;
        margin-top: 10em;
      }

      .v-input__details, p {
        text-align: center;
      }

      .v-text-field {
        margin-top: .5em;
        border-color: $primary;
      }

      .v-btn {
        display: flex;
        align-self: center;
        width: 100%;
        margin-top: .5em;
        padding: 2em 0;
        background: $primary;
        color: $bg;
        font-weight: 800;
      }

      .login {
        color: $primary;
      }

      .login:hover {
        color: #0073FF;
      }

      .v-container > *:last-child {
        margin-top: 1em;
      }
    }
}
</style>
