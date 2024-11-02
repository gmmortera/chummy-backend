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
    themes: {
      light: {
        colors: {
          primary: '#FF8C00',
          secondary: '#4682B4',
          accent: '#32CD32',
          bg: '#F5F5F5',
          text: '#333333',
          hl: '#FFF0E0',
          borderLight: '#E0E0E0',
          borderMedium: '#BDBDBD'
        }
      }
    }
  },
})
