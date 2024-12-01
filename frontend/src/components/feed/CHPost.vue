<template>
  <v-card
    prepend-avatar="https://cdn.vuetifyjs.com/images/john.jpg"
    :title="getName(idUser)"
    :subtitle="new Date(createdAt).toDateString()"
    width="35rem"
    variant="tonal"
  >
    <v-card-text>
      {{ content }}
    </v-card-text>
    <v-img
      :src="image"
    />
    <v-card-actions>
      <v-btn>
        <v-icon icon="mdi-heart-outline"></v-icon>
      </v-btn>
      <v-btn>
        <v-icon icon="mdi-message-text"></v-icon>
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script lang='ts' setup>
import type { Post } from '../../types/post'

const { getName } = useUserStore()

defineProps<Post>()

</script>

<style lang='scss' scoped>
.v-card {
  margin-top: 1rem;
}
</style>