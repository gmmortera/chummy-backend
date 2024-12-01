import { NewUser } from '@/types/user'
import { AxiosError } from 'axios'

const resource = "/sessions"

export const useAuthStore = defineStore('authentication', () => {
  const post = async (form: NewUser): Promise<number | undefined> => {
    try {
      const { status } = await axios.post(resource, form)
      return status
    } catch (error) {
      const e = error as Error | AxiosError
      console.error(e)
    }
  }

  const destroy = async (): Promise<undefined> => {
    try {
      await axios.delete(resource)
    } catch (error) {
      const e = error as Error | AxiosError
      console.error(e)
    }
  }

  return {
    post,
    destroy
  }
})