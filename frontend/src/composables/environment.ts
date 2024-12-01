export const useEnvironment = () => {
  const userStore = useUserStore()
  const postStore = usePostStore()

  const fetch = () => {
    userStore.fetch()
    postStore.fetch()
  }

  return {
    fetch
  }
}