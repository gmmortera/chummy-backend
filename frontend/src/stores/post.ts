import { AxiosError } from "axios"
import type { Post, UserPost } from '../types/post'

const resource = "/posts"

export const usePostStore = defineStore('post', () => {
  const _posts = ref<Post[]>([])

  const fetch = async () => {
    try {
      const { data } = await axios.get(resource)
      const posts = data.posts

      _posts.value = posts as Post[]

    } catch (e) {
      const error = e as Error | AxiosError
      console.error(error)
    }
  }

  const post = async (post: UserPost) => {
    try {
      const { status, data } = await axios.post(resource, post)
      const newPost = data.post

      if (status === 201) _posts.value.push(newPost as Post)

    } catch(e) {
      const error = e as Error | AxiosError
      console.error(error)
    }
  }

  const destroy = async (id: string) => {
    try {
      const { status } = await axios.delete(resource + `/${id}`)
      
      if (status === 404) _posts.value = _posts.value.filter((p) => p.id !== id)

    } catch (e) {
      const error = e as Error | AxiosError
      console.log(error)
    }
  }

  const get = computed(() => {
    return _posts.value
  })

  return {
    get,
    fetch,
    post,
    destroy
  }
})