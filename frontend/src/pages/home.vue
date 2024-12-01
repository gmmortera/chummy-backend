<template>
  <section>

  </section>
  <nav>

  </nav>
  <section
    class="feed"
  >
    <div>
      <CHPostInput 
        @post="post"
      />
      <CHPost
        v-for="(post, index) in feed"
        :key="index"
        v-bind="postProp(post)"
      />
    </div>
  </section>
  <aside>

  </aside>
</template>

<script lang='ts' setup>
import { useEnvironment } from '@/composables/environment';
import { Post } from '@/types/post'

const postStore = usePostStore()
const { fetch } = useEnvironment()

const posted = reactive({
  image: '',
  content: ''
})

const post = (content: string) => {
  posted.content = content
  postStore.post(posted)
}

const feed = computed(() => {
  return postStore.get.toReversed()
})

const postProp = (post: Post) => ({
  id: post.id,
  idUser: post.idUser,
  content: post.content,
  image: post.image,
  createdAt: post.createdAt
})

onMounted(() => {
  fetch()
})
</script>

<style lang='scss' scoped>
.feed {
  display: grid;
  place-items: center;
}

.v-form {
  min-width: 35rem;
}
</style>