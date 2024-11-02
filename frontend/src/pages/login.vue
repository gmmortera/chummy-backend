<template>
  <div class="login">
    <section class="login__left">
      <div class="logo">
        <v-img
          contain
          :width="48"
          src="/assets/chummy-logo.svg"
        />
        <h1>CHUMMY</h1>
      </div>
      <v-form>
        <v-container>
          <v-text-field
            v-model="loginForm.email" 
            :rules="rules.email"
            label="Email"
            type="email"
            required
          ></v-text-field>
          <v-text-field
            v-model="loginForm.password"
            :rules="rules.password"
            label="Password"
            type="password"
            required
          ></v-text-field>
          <v-btn
            @click="login"
          >
            Log in
          </v-btn>
          <p>Don't have an account? <router-link class="register" :to="{ path: '/register' }">Sign up for free</router-link></p>
        </v-container>
      </v-form>
    </section>
    <section class="login__right">
      <div class="logo">
        <v-img
          contain
          :width="144"
          src="/assets/logo-with-circle.svg"
        />
        <h1>CHUMMY</h1>
      </div>
      <h2 class="slogan">"Where every post is a friendly chat"</h2>
    </section>
  </div>
</template>

<script lang="ts" setup>
  import { useRules } from '../composables/rules'

  const loginForm = reactive({
    email: '',
    password: ''
  })

  const { post } = useAuthStore()
  const { push } = useRouter()
  const rules = useRules()

  const login = async () => {
    try {
      const status = await post(loginForm)
      
      if (status === 200) {
        push('/')
      }
    } catch (error) {
      const e = error as Error
      console.error(e)
    }
  }
</script>

<style lang="scss" scoped>
.login {
  display: flex;
  justify-content: space-between;
  height: 100vh;
  background: $primary;

  h1 {
    font-weight: 900;
    margin-left: .5rem;
  }

  &__left {
    width: 40%;
    background: $bg;
    border-radius: 0 10% 10% 0;

    .logo {
      display: inline-flex;
      margin-top: 1rem;
      padding-left: 1.5rem;

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

    .register {
      color: $primary;
    }

    .register:hover {
      color: #0073FF;
    }

    .v-container > *:last-child {
      margin-top: 1em;
    }
  }

  &__right {
    width: 60%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    overflow: hidden;
    color: $bg;

    .logo {
      display: inline-flex;
      margin-top: 1em;
      max-width: 100%;
      gap: 1em;

      h1 {
        font-size: 7em;
      }
    }

    .slogan {
      font-size: 1.5em;
    }
  }
}
</style>
