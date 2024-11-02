import type { NewUser } from "@/types/storeTypes"
import { AxiosError } from "axios"

const resource = "/users"

export const useUserStore = defineStore('user', () => {

  const post = async (form: NewUser): Promise<number | undefined> => {
    try {
      const { status } = await axios.post(resource, form)
      return status
    } catch(e) {
      const error = e as Error | AxiosError
      console.error(error)
    }
  }

  return {
    post
  }
  
})