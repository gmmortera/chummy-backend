<template>
  <v-container>
    <v-form
      @submit.prevent="post"
    >
      <v-row no-gutters justify="space-between">
        <v-col cols="12">
          <v-text-field
            v-model.lazy="postContent"
            type="text"
            placeholder="What's on your mind?"
            variant="outlined"
            hide-details
          />
        </v-col>
        <input 
          type="file" 
          ref="fileInput" 
          style="display: none" 
          @change="handleFileUpload"
          hidden
          accept="'image/jpg', 
          'image/jpeg', 
          'image/png', 
          'image/gif', 
          'image/svg'"
        >
        <v-btn
          class="images"
          @click="triggerFileInput"
          variant="plain"
        >
          <v-icon>mdi-image-area</v-icon>
        </v-btn>
        <v-btn
          class="input"
          color="primary"
          type="submit"
        >Post</v-btn>
      </v-row>
    </v-form>
  </v-container>
</template>

<script lang='ts' setup>

interface ImageForm {
  image: string | ArrayBuffer | null
  title: string
}

const postContent = ref('')
const fileInput = ref<HTMLInputElement | null>(null)
const imageForm = ref<ImageForm[]>([])

const emit = defineEmits<{
  post: [string]
}>()

const post = () => {
  emit('post', postContent.value)
  postContent.value = ''
}

const triggerFileInput = () => {
  fileInput.value?.click()
}

const handleFileUpload = () => {
  let data = fileInput.value as HTMLInputElement
  const file = data.files![0]
  const reader = new FileReader()

  reader.onload = () => {
    const image = {
      image: reader.result,
      title: file.name
    }
    imageForm.value.push(image)
  }
  reader.readAsDataURL(file)
}

</script>

<style lang='scss' scoped>
.v-container {
  margin: 2rem 0 3rem 0;
  background-color: var($bg);
}

.v-btn {
  margin-top: 1rem;
}
</style>