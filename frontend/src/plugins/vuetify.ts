/**
 * plugins/vuetify.ts
 *
 * Framework documentation: https://vuetifyjs.com`
 */

// Styles
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'
import colors from '../styles/settings.scss'

// Composables
import { createVuetify } from 'vuetify'

// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
export default createVuetify({
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        dark: false,
        colors: {
          primary: '#FF8C00',
          secondary: '#4682B4',
          accent: '#32CD32',
          bg: '#F5F5F5',
          text: '#333333',
          hl: '#FFF0E0',
          border: '#E0E0E0',
          'border-medium': '#BDBDBD'
        }
      },
      dark: {
        dark: true,
        colors: {
          primary: '#FFA500',
          secondary: '#5F9EA0',
          accent: '#3CB371',
          background: '#121212',
          surface: '#1E1E1E',
          text: '#E0E0E0',
          highlight: '#2C2C2C',
          border: '#404040',
          'border-medium': '#606060'
        }
      }
    }
  },
})
